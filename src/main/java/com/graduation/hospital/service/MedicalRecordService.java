package com.graduation.hospital.service;

import com.graduation.hospital.entity.MedicalRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 病历记录服务接口
 * 定义病历记录的业务操作
 */
public interface MedicalRecordService {

    /**
     * 创建病历记录
     * 自动生成病历编号：MR + 年月日 + 5位序号
     */
    MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

    /**
     * 根据 ID 获取病历记录
     */
    MedicalRecord getMedicalRecordById(Long id);

    /**
     * 根据病历编号获取病历记录
     * @param recordNo 病历编号
     */
    MedicalRecord getMedicalRecordByRecordNo(String recordNo);

    /**
     * 获取所有病历记录
     */
    List<MedicalRecord> getAllMedicalRecords();

    /**
     * 根据患者 ID 获取病历记录列表
     * @param patientId 患者 ID
     */
    List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId);

    /**
     * 根据医生 ID 获取病历记录列表
     * @param doctorId 医生 ID
     */
    List<MedicalRecord> getMedicalRecordsByDoctorId(Long doctorId);

    /**
     * 根据时间范围获取病历记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    List<MedicalRecord> getMedicalRecordsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新病历记录
     */
    MedicalRecord updateMedicalRecord(Long id, MedicalRecord medicalRecord);

    /**
     * 删除病历记录（逻辑删除）
     */
    void deleteMedicalRecord(Long id);
}