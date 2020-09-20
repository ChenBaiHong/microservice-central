package com.baimicro.central.db.config;

import com.baimicro.central.db.properties.MybatisPlusAutoFillProperties;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDate;
import java.util.Date;

/**
 * @project: hospital-cloud-platform
 * @author: chen.baihoo
 * @date: 2020/2/16
 * @Description: TODO 自定义填充公共 name 字段
 * version 0.1
 */
public class DateMetaObjectHandler implements MetaObjectHandler {
    private MybatisPlusAutoFillProperties autoFillProperties;

    public DateMetaObjectHandler(MybatisPlusAutoFillProperties autoFillProperties) {
        this.autoFillProperties = autoFillProperties;
    }

    /**
     * 是否开启了插入填充
     */
    @Override
    public boolean openInsertFill() {
        return autoFillProperties.getEnableInsertFill();
    }

    /**
     * 是否开启了更新填充
     */
    @Override
    public boolean openUpdateFill() {
        return autoFillProperties.getEnableUpdateFill();
    }

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(autoFillProperties.getCreateTimeField(), metaObject);
        Object updateTime = getFieldValByName(autoFillProperties.getUpdateTimeField(), metaObject);
        if (createTime == null || updateTime == null) {
            LocalDate date = LocalDate.now();
            if (createTime == null) {
                setFieldValByName(autoFillProperties.getCreateTimeField(), date, metaObject);
            }
            if (updateTime == null) {
                setFieldValByName(autoFillProperties.getUpdateTimeField(), date, metaObject);
            }
        }
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(autoFillProperties.getUpdateTimeField(), new Date(), metaObject);
    }
}
