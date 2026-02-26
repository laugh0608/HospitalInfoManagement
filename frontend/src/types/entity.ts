/** 基础实体 */
export interface BaseEntity {
  id: number;
  createdAt: string;
  updatedAt: string;
}

/** 系统用户 */
export interface SysUser extends BaseEntity {
  username: string;
  email: string;
  enabled: boolean;
  profile: SysUserProfile | null;
  roles: SysRole[];
}

/** 用户详情 */
export interface SysUserProfile extends BaseEntity {
  realName: string;
  phone: string;
  avatar: string;
  gender: string;
  birthDate: string;
}

/** 系统角色 */
export interface SysRole extends BaseEntity {
  name: string;
  code: string;
  description: string;
}

/** 系统权限 */
export interface SysPermission extends BaseEntity {
  name: string;
  code: string;
  description: string;
}

/** 患者 */
export interface Patient extends BaseEntity {
  patientNo: string;
  name: string;
  gender: string;
  birthDate: string;
  idCard: string;
  phone: string;
  address: string;
  insuranceType: string;
  allergies: string;
  medicalHistory: string;
  emergencyContact: string;
  emergencyPhone: string;
}

/** 医生 */
export interface Doctor extends BaseEntity {
  doctorNo: string;
  name: string;
  title: string;
  specialty: string;
  yearsOfExperience: number;
  department: Department | null;
  isAvailable: boolean;
  photo: string;
  introduction: string;
}

/** 科室 */
export interface Department extends BaseEntity {
  name: string;
  code: string;
  description: string;
  location: string;
  phone: string;
}

/** 药品 */
export interface Medicine extends BaseEntity {
  medicineCode: string;
  name: string;
  specification: string;
  unit: string;
  price: number;
  category: string;
  stock: number;
  minStock: number;
  manufacturer: string;
  approvalNumber: string;
  storageConditions: string;
  description: string;
}

/** 病历记录 */
export interface MedicalRecord extends BaseEntity {
  recordNo: string;
  patient: Patient | null;
  doctor: Doctor | null;
  visitTime: string;
  visitType: string;
  chiefComplaint: string;
  diagnosis: string;
  treatmentPlan: string;
  prescription: string;
  remarks: string;
  followUpDate: string;
}

/** 预约挂号 */
export interface Appointment extends BaseEntity {
  appointmentNo: string;
  patient: Patient | null;
  doctor: Doctor | null;
  department: Department | null;
  appointmentTime: string;
  status: string;
  reason: string;
  remarks: string;
  cancelTime: string;
  cancelReason: string;
}
