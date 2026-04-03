Hospital Management System domain relationships:

- One Appointment has one MedicalRecord
- One MedicalRecord has one Prescription
- One Prescription has many PrescriptionDetails
- One Medicine can be used in many PrescriptionDetails

- One Patient has many Bills
- One Bill has many BillMedicineDetails
- One Medicine can appear in many BillMedicineDetails

Architecture:
- Entity → DAO → Service → Controller
- DAO uses Hibernate
- Service handles business logic