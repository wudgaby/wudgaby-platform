package com.wudgaby.downloadsample;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 市民基础属性
 * </p>
 *
 * @author anymo
 * @since 2022-02-24
 */
@Data
@TableName("biz_people")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String cnName;

    private String enName;

    private String idCardNo;

    private String mobile;

    private String email;

    private String address;

    private Integer age;

    private String sex;

    @ExcelIgnore
    private LocalDate dob;

    private String nat;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        People people = (People) o;
        return Objects.equal(idCardNo, people.idCardNo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idCardNo);
    }


}
