package com.panyu.yupictureback.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panyu.yupictureback.common.ResponseResult;
import com.panyu.yupictureback.domain.dto.picture.PictureEditDTO;
import com.panyu.yupictureback.domain.dto.picture.PictureQueryDTO;
import com.panyu.yupictureback.domain.dto.picture.PictureUpdateDTO;
import com.panyu.yupictureback.domain.dto.picture.PictureUploadDTO;
import com.panyu.yupictureback.domain.entity.Picture;
import com.panyu.yupictureback.domain.entity.User;
import com.panyu.yupictureback.domain.vo.picture.PictureTagCategoryVO;
import com.panyu.yupictureback.domain.vo.picture.PictureVO;
import com.panyu.yupictureback.domain.vo.user.UserLoginVO;
import com.panyu.yupictureback.enums.ErrorCodeEnum;
import com.panyu.yupictureback.exception.BusinessException;
import com.panyu.yupictureback.manager.FileManager;
import com.panyu.yupictureback.mapper.PictureMapper;
import com.panyu.yupictureback.service.PictureService;
import com.panyu.yupictureback.service.UserService;
import com.panyu.yupictureback.utils.ResultUtil;
import com.panyu.yupictureback.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private FileManager fileManager;

    @Resource
    private UserService userService;

    @Override
    public ResponseResult<PictureVO> uploadPicture(MultipartFile multipartFile, Long pictureId, UserLoginVO loginUser) {
        ThrowUtil.throwIf(loginUser == null, ErrorCodeEnum.NO_AUTH_ERROR);
        ThrowUtil.throwIf(multipartFile == null, ErrorCodeEnum.PARAMS_ERROR, "文件不能为空");
        // 定义上传路径前缀
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        // 上传图片
        PictureUploadDTO uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        // 封装图片实体
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getName());
        picture.setSize(uploadPictureResult.getSize());
        picture.setWidth(uploadPictureResult.getWidth());
        picture.setHeight(uploadPictureResult.getHeight());
        picture.setScale(uploadPictureResult.getScale());
        picture.setFormat(uploadPictureResult.getFormat());
        picture.setUserId(loginUser.getId());
        // 判断是新增还是修改
        if (pictureId != null) {
            picture.setId(pictureId);
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
        UserLoginVO userLoginVO = userService.getCurrentUser().getData();
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
        UserLoginVO userLoginVO = userService.getCurrentUser().getData();
        // 判断是否存在
        long id = pictureEditDTO.getId();
        Picture oldPicture = this.getById(id);
        ThrowUtil.throwIf(oldPicture == null, ErrorCodeEnum.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldPicture.getUserId().equals(userLoginVO.getId()) && !userService.isAdmin(userLoginVO)) {
            throw new BusinessException(ErrorCodeEnum.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = this.updateById(picture);
        ThrowUtil.throwIf(!result, ErrorCodeEnum.OPERATION_ERROR);
        return ResultUtil.success(true);
    }

    @Override
    public ResponseResult<Page<PictureVO>> listPictureVOByPage(PictureQueryDTO pictureQueryDTO) {
        long current = pictureQueryDTO.getCurrent();
        long size = pictureQueryDTO.getPageSize();
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
        List<String> tagList = Arrays.asList("热门", "搞笑", "生活", "高清", "艺术", "校园", "背景", "简历", "创意");
        List<String> categoryList = Arrays.asList("模板", "电商", "表情包", "素材", "海报");
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

    /**
     * 分页获取图片封装
     */
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
}




