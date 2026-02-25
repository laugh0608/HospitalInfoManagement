package com.graduation.hospital.service;

import com.graduation.hospital.entity.Medicine;

import java.util.List;

/**
 * 药品服务接口
 * 定义药品管理的业务操作
 */
public interface MedicineService {

    /**
     * 添加药品
     * 自动生成药品编码：M + 年月日 + 5位序号
     */
    Medicine createMedicine(Medicine medicine);

    /**
     * 根据 ID 获取药品信息
     */
    Medicine getMedicineById(Long id);

    /**
     * 根据药品编码获取药品信息
     * @param medicineCode 药品编码
     */
    Medicine getMedicineByMedicineCode(String medicineCode);

    /**
     * 获取所有药品列表
     */
    List<Medicine> getAllMedicines();

    /**
     * 搜索药品（按名称、编码、规格模糊匹配）
     * @param keyword 关键字
     */
    List<Medicine> searchMedicines(String keyword);

    /**
     * 根据分类获取药品列表
     * @param category 分类（西药、中药、保健品）
     */
    List<Medicine> getMedicinesByCategory(String category);

    /**
     * 获取库存不足的药品列表（低于库存预警值）
     */
    List<Medicine> getLowStockMedicines();

    /**
     * 更新药品信息
     */
    Medicine updateMedicine(Long id, Medicine medicine);

    /**
     * 删除药品（逻辑删除）
     */
    void deleteMedicine(Long id);

    /**
     * 更新药品库存
     * @param id 药品 ID
     * @param quantity 库存变更数量（正数增加，负数减少）
     */
    Medicine updateStock(Long id, int quantity);

    /**
     * 检查药品编码是否存在
     */
    boolean existsByMedicineCode(String medicineCode);
}