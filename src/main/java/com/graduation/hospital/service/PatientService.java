package com.graduation.hospital.service;

import com.graduation.hospital.entity.Patient;

import java.util.List;

/**
 * 病人信息服务接口
 * 定义病人档案的业务操作
 */
public interface PatientService {

    /**
     * 创建病人档案
     * 自动生成病人编号：P + 年月日 + 5位序号
     */
    Patient createPatient(Patient patient);

    /**
     * 根据 ID 获取病人信息
     */
    Patient getPatientById(Long id);

    /**
     * 根据病人编号获取病人信息
     * @param patientNo 病人编号（如：P2026022500001）
     */
    Patient getPatientByPatientNo(String patientNo);

    /**
     * 获取所有病人列表
     */
    List<Patient> getAllPatients();

    /**
     * 搜索病人（按姓名、手机号、身份证号模糊匹配）
     * @param keyword 关键字
     */
    List<Patient> searchPatients(String keyword);

    /**
     * 更新病人信息
     */
    Patient updatePatient(Long id, Patient patient);

    /**
     * 删除病人档案（逻辑删除）
     */
    void deletePatient(Long id);

    /**
     * 检查病人编号是否存在
     */
    boolean existsByPatientNo(String patientNo);

    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
}