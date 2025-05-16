package com.panyu.yupictureback.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.picture.*;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.picture.PictureTagCategoryVO;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.enums.PictureReviewStatusEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.manager.upload.FilePictureUpload;
import com.panyu.yupictureback.manager.upload.UrlPictureUpload;
import com.panyu.yupictureback.mapper.PictureMapper;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.service.UserService;
import com.panyu.yupictureback.template.PictureUploadTemplate;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import com.panyu.yupictureback.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yupan
 * @description 针对表【picture(图片)】的数据库操作Service实现
 * @createDate 2024-12-22 17:04:15
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {
    @Resource
    private UserService userService;
    @Resource
    private FilePictureUpload filePictureUpload;
    @Resource
    private UrlPictureUpload urlPictureUpload;

    @Override
    public ResponseResult<PictureVO> uploadPicture(Object inputSource, PictureUploadDTO pictureUploadDTO, UserLoginVO loginUser) {
        ThrowUtil.throwIf(loginUser == null, ErrorCodeEnum.NO_AUTH_ERROR);
        ThrowUtil.throwIf(inputSource == null, ErrorCodeEnum.PARAMS_ERROR, "输入源不能为空");
        // 如果是更新图片，需要校验图片是否存在
        if (pictureUploadDTO != null && pictureUploadDTO.getId() != null) {
            Picture oldPicture = this.getById(pictureUploadDTO.getId());
            ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR, "图片不存在");
            // 仅本人或管理员可编辑
            if (!oldPicture.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
                throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
            }
        }
        // 定义上传路径前缀
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        // 上传图片（根据类型选择不同的上传方式）
        PictureUploadTemplate pictureUploadTemplate = inputSource instanceof MultipartFile
                ? filePictureUpload : urlPictureUpload;
        PictureUploadDTO uploadPictureResult = pictureUploadTemplate.uploadPicture(inputSource, uploadPathPrefix);
        // 封装图片实体
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        if (pictureUploadDTO != null && StrUtil.isNotBlank(pictureUploadDTO.getName())) {
            picture.setName(pictureUploadDTO.getName());
        } else {
            picture.setName(uploadPictureResult.getName());
        }
        picture.setSize(uploadPictureResult.getSize());
        picture.setWidth(uploadPictureResult.getWidth());
        picture.setHeight(uploadPictureResult.getHeight());
        picture.setScale(uploadPictureResult.getScale());
        picture.setFormat(uploadPictureResult.getFormat());
        picture.setUserId(loginUser.getId());
        this.fillReviewParams(picture, loginUser);
        // 判断是新增还是修改
        if (pictureUploadDTO != null && pictureUploadDTO.getId() != null) {
            picture.setId(pictureUploadDTO.getId());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR, "数据库操作失败！");
        return ResultUtil.success(PictureVO.objToVo(picture));
    }

    @Override
    public ResponseResult<Boolean> deletePicture(Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        UserLoginVO userLoginVO = UserContextUtil.getCurrentUser();
        // 判断是否存在
        Picture oldPicture = this.getById(id);
        ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldPicture.getUserId().equals(userLoginVO.getId()) && !userService.isAdmin(userLoginVO)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.removeById(id);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR);
        return ResultUtil.success(true);
    }

    @Override
    public ResponseResult<Boolean> updatePicture(PictureUpdateDTO pictureUpdateDTO) {
        if (pictureUpdateDTO == null || pictureUpdateDTO.getId() <= 0) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        // 将实体类和 DTO 进行转换
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureUpdateDTO, picture);
        // 注意将 list 转为 string
        picture.setTags(JSONUtil.toJsonStr(pictureUpdateDTO.getTags()));
        // 数据校验
        this.validPicture(picture);
        // 判断是否存在
        long id = pictureUpdateDTO.getId();
        Picture oldPicture = this.getById(id);
        ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        this.fillReviewParams(picture, UserContextUtil.getCurrentUser());
        // 操作数据库
        boolean result = this.updateById(picture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR);
        return ResultUtil.success(true);
    }

    @Override
    public ResponseResult<Picture> getPictureById(Long id) {
        ThrowUtil.throwIf(id == null || id <= 0, ErrorCodeEnum.PARAMS_ERROR);
        // 查询数据库
        Picture picture = this.getById(id);
        ThrowUtil.throwIf(picture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtil.success(picture);
    }

    @Override
    public ResponseResult<PictureVO> getPictureVOById(Long id) {
        ThrowUtil.throwIf(id == null || id <= 0, ErrorCodeEnum.PARAMS_ERROR);
        // 查询数据库
        Picture picture = this.getById(id);
        ThrowUtil.throwIf(picture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtil.success(this.getPictureVO(picture));
    }

    @Override
    public ResponseResult<Page<Picture>> listPictureByPage(PictureQueryDTO pictureQueryDTO) {
        long current = pictureQueryDTO.getCurrent();
        long size = pictureQueryDTO.getPageSize();
        // 查询数据库
        Page<Picture> picturePage = this.page(new Page<>(current, size),
                this.getQueryWrapper(pictureQueryDTO));
        picturePage.getRecords().forEach(picture -> {
            Long userId = picture.getUserId();
            picture.setUsername(userService.getById(userId).getUsername());
        });
        return ResultUtil.success(picturePage);
    }

    @Override
    public ResponseResult<Boolean> editPicture(PictureEditDTO pictureEditDTO) {
        // 在此处将实体类和 DTO 进行转换
        Picture picture = new Picture();
        BeanUtils.copyProperties(pictureEditDTO, picture);
        // 注意将 list 转为 string
        picture.setTags(JSONUtil.toJsonStr(pictureEditDTO.getTags()));
        // 数据校验
        this.validPicture(picture);
        UserLoginVO userLoginVO = UserContextUtil.getCurrentUser();
        // 判断是否存在
        long id = pictureEditDTO.getId();
        Picture oldPicture = this.getById(id);
        ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldPicture.getUserId().equals(userLoginVO.getId()) && !userService.isAdmin(userLoginVO)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        this.fillReviewParams(picture, userLoginVO);
        // 操作数据库
        boolean result = this.updateById(picture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR);
        return ResultUtil.success(true);
    }

    @Override
    public ResponseResult<Page<PictureVO>> listPictureVOByPage(PictureQueryDTO pictureQueryDTO) {
        long current = pictureQueryDTO.getCurrent();
        long size = pictureQueryDTO.getPageSize();
        // 仅查询审核通过的图片
        pictureQueryDTO.setReviewStatus(PictureReviewStatusEnum.PASS.getValue());
        // 限制爬虫
        ThrowUtil.throwIf(size > 20, ErrorCodeEnum.PARAMS_ERROR);
        // 查询数据库
        Page<Picture> picturePage = this.page(new Page<>(current, size),
                this.getQueryWrapper(pictureQueryDTO));
        // 获取封装类
        return ResultUtil.success(this.getPictureVOPage(picturePage));
    }

    @Override
    public ResponseResult<PictureTagCategoryVO> listPictureTagCategory() {
        PictureTagCategoryVO pictureTagCategoryVO = new PictureTagCategoryVO();
        List<String> tagList = Arrays.asList("热门", "搞笑", "生活", "高清", "艺术", "校园", "背景", "简历", "创意", "网页");
        List<String> categoryList = Arrays.asList("模板", "电商", "表情包", "素材", "海报", "抓图");
        pictureTagCategoryVO.setTagList(tagList);
        pictureTagCategoryVO.setCategoryList(categoryList);
        return ResultUtil.success(pictureTagCategoryVO);
    }


    @Override
    public PictureVO getPictureVO(Picture picture) {
        // 对象转封装类
        PictureVO pictureVO = PictureVO.objToVo(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserLoginVO userVO = new UserLoginVO();
            BeanUtils.copyProperties(user, userVO);
            pictureVO.setUser(userVO);
        }
        return pictureVO;
    }

    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVO> pictureVOPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        if (CollUtil.isEmpty(pictureList)) {
            return pictureVOPage;
        }
        // 对象列表 => 封装对象列表
        List<PictureVO> pictureVOList = pictureList.stream().map(PictureVO::objToVo).collect(Collectors.toList());
        // 1. 关联查询用户信息
        Set<Long> userIdSet = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            UserLoginVO userVO = new UserLoginVO();
            assert user != null;
            BeanUtils.copyProperties(user, userVO);
            pictureVO.setUser(userVO);
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }


    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryDTO pictureQueryDTO) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if (pictureQueryDTO == null) {
            return queryWrapper;
        }
        String createTimeFrom = "";
        String createTimeTo = "";
        if (pictureQueryDTO.getCreateTimeRange() != null && pictureQueryDTO.getCreateTimeRange().length == 2) {
            createTimeFrom = pictureQueryDTO.getCreateTimeRange()[0];
            createTimeTo = pictureQueryDTO.getCreateTimeRange()[1];
        }
        // 从对象中取值
        String name = pictureQueryDTO.getName();
        String introduction = pictureQueryDTO.getIntroduction();
        String category = pictureQueryDTO.getCategory();
        List<String> tags = pictureQueryDTO.getTags();
        Long picSize = pictureQueryDTO.getSize();
        Integer picWidth = pictureQueryDTO.getWidth();
        Integer picHeight = pictureQueryDTO.getHeight();
        Double picScale = pictureQueryDTO.getScale();
        String picFormat = pictureQueryDTO.getFormat();
        String searchText = pictureQueryDTO.getSearchText();
        Long userId = pictureQueryDTO.getUserId();
        Integer reviewStatus = pictureQueryDTO.getReviewStatus();
        String reviewMessage = pictureQueryDTO.getReviewMessage();
        Long reviewerId = pictureQueryDTO.getReviewerId();
        // 从多字段中搜索
        if (CharSequenceUtil.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("introduction", searchText)
            );
        }
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(CharSequenceUtil.isNotBlank(name), "name", name);
        queryWrapper.like(CharSequenceUtil.isNotBlank(introduction), "introduction", introduction);
        queryWrapper.like(CharSequenceUtil.isNotBlank(picFormat), "format", picFormat);
        queryWrapper.eq(CharSequenceUtil.isNotBlank(category), "category", category);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picWidth), "width", picWidth);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picHeight), "height", picHeight);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picSize), "size", picSize);
        queryWrapper.eq(ObjectUtil.isNotEmpty(picScale), "scale", picScale);
        queryWrapper.eq(ObjectUtil.isNotEmpty(reviewStatus), "review_status", reviewStatus);
        queryWrapper.like(CharSequenceUtil.isNotBlank(reviewMessage), "review_message", reviewMessage);
        queryWrapper.eq(ObjectUtil.isNotEmpty(reviewerId), "reviewer_id", reviewerId);
        queryWrapper.ge(ObjectUtil.isNotEmpty(createTimeFrom), "create_time", createTimeFrom);
        queryWrapper.le(ObjectUtil.isNotEmpty(createTimeTo), "create_time", createTimeTo);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", String.format("\"%s\"", tag));
            }
        }
        // 排序
        queryWrapper.orderBy(ObjectUtil.isNotEmpty(picSize), false, "create_time", "size", "name");
        return queryWrapper;
    }


    @Override
    public void validPicture(Picture picture) {
        ThrowUtil.throwIf(picture == null, ErrorCodeEnum.PARAMS_ERROR);
        // 从对象中取值
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时，id 不能为空，有参数则校验
        ThrowUtil.throwIf(ObjectUtil.isNull(id), ErrorCodeEnum.PARAMS_ERROR, "id 不能为空");
        if (CharSequenceUtil.isNotBlank(url)) {
            ThrowUtil.throwIf(url.length() > 1024, ErrorCodeEnum.PARAMS_ERROR, "url 过长");
        }
        if (CharSequenceUtil.isNotBlank(introduction)) {
            ThrowUtil.throwIf(introduction.length() > 800, ErrorCodeEnum.PARAMS_ERROR, "简介过长");
        }
    }

    @Override
    public void doPictureReview(PictureReviewDTO pictureReviewDTO, UserLoginVO loginUser) {
        Long id = pictureReviewDTO.getId();
        Integer reviewStatus = pictureReviewDTO.getReviewStatus();
        PictureReviewStatusEnum reviewStatusEnum = PictureReviewStatusEnum.getEnumByValue(reviewStatus);
        if (id == null || reviewStatusEnum == null || PictureReviewStatusEnum.REVIEWING.equals(reviewStatusEnum)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR);
        }
        // 判断是否存在
        Picture oldPicture = this.getById(id);
        ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 已是该状态
        if (oldPicture.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        Picture updatePicture = new Picture();
        BeanUtils.copyProperties(pictureReviewDTO, updatePicture);
        updatePicture.setReviewerId(loginUser.getId());
        updatePicture.setReviewTime(LocalDateTime.now().toString());
        boolean result = this.updateById(updatePicture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR);
    }

    @Override
    public void fillReviewParams(Picture picture, UserLoginVO loginUser) {
        if (userService.isAdmin(loginUser)) {
            // 管理员自动过审
            picture.setReviewStatus(PictureReviewStatusEnum.PASS.getValue());
            picture.setReviewerId(loginUser.getId());
            picture.setReviewMessage("管理员自动过审");
            picture.setReviewTime(LocalDateTime.now().toString());
        } else {
            // 非管理员，创建或编辑都要改为待审核
            picture.setReviewStatus(PictureReviewStatusEnum.REVIEWING.getValue());
        }
    }

    @Override
    public Integer batchUploadPicture(PictureBatchUploadDTO pictureBatchUploadDTO, UserLoginVO loginUser) {
        if (!userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        String searchText = pictureBatchUploadDTO.getSearchText();
        Integer batchSize = pictureBatchUploadDTO.getBatchSize();
        // 判断抓取的数量，最多30条
        if (batchSize > 30) {
            throw new BusinessException(ErrorCodeEnum.PARAMS_ERROR, "最多上传30张图片");
        }
        String fetchRul = String.format("https://cn.bing.com/images/async?q=%s&mmasync=1", searchText);
        Document document;
        try {
            document = Jsoup.connect(fetchRul).get();
        } catch (IOException e) {
            log.error("批量抓取获取页面失败！", e);
            throw new BusinessException(ErrorCodeEnum.OPERATION_ERROR, "批量抓取获取页面失败！");
        }
        // 解析内容
        Element firstEle = document.getElementsByClass("dgControl").first();
        if (Objects.isNull(firstEle)) {
            throw new BusinessException(ErrorCodeEnum.OPERATION_ERROR, "获取首个元素失败！");
        }
        Elements elements = firstEle.select("img.mimg");
        if (CollUtil.isEmpty(elements)) {
            throw new BusinessException(ErrorCodeEnum.OPERATION_ERROR, "获取图片为空！");
        }
        int uploadCount = 0;
        for (Element element : elements) {
            String fileUrl = element.attr("src");
            if (StrUtil.isBlank(fileUrl)) {
                log.info("当前链接为空，已跳过: {}", fileUrl);
                continue;
            }
            // 处理图片上传地址，防止出现转义问题
            int questionMarkIndex = fileUrl.indexOf("?");
            if (questionMarkIndex > -1) {
                fileUrl = fileUrl.substring(0, questionMarkIndex);
            }
            try {
                PictureUploadDTO pictureUploadDTO = new PictureUploadDTO();
                pictureUploadDTO.setName(searchText + "-" + RandomUtil.randomString(10));
                PictureVO pictureVO = this.uploadPicture(fileUrl, pictureUploadDTO, loginUser).getData();
                log.info("图片上传成功, id = {}", pictureVO.getId());
                Picture picture = new Picture();
                picture.setId(pictureVO.getId());
                picture.setIntroduction("抓取图片");
                picture.setCategory("抓图");
                picture.setTags("[\"网页\"]");
                this.updateById(picture);
                uploadCount++;
            } catch (Exception e) {
                log.error("图片上传失败", e);
                continue;
            }
            if (uploadCount >= pictureBatchUploadDTO.getBatchSize()) {
                break;
            }
        }
        return uploadCount;
    }
}







