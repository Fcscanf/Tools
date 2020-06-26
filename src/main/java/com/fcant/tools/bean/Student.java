package com.fcant.tools.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Student
 * <p>
 * encoding:UTF-8
 *
 * @author Fcant 下午 22:55 2020/5/7/0007
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String studentNum;
    private String studentName;
    private String workCompany;

    private String identityNumber;
    private String password;
    private String cookie;

}
