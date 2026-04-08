INSERT INTO departments (name) VALUES
('Cardiology'),
('Neurology'),
('General Medicine');

INSERT INTO doctors (full_name, department_id) VALUES
('Dr. Nguyen Van A', 1),
('Dr. Tran Thi B', 2),
('Dr. Le Van C', 3);

INSERT INTO patients (full_name, dob, gender, phone) VALUES
('Pham Duc Phat', '2003-05-12', 'MALE', '0901234567'),
('Nguyen Thi Hoa', '1998-11-20', 'FEMALE', '0912345678'),
('Tran Van Nam', '1985-02-15', 'MALE', '0923456789');

INSERT INTO appointments (patient_id, doctor_id, appointment_time, status) VALUES
(1, 1, '2026-04-01 09:00:00', 'DONE'),
(2, 2, '2026-04-01 10:00:00', 'PENDING'),
(3, 3, '2026-04-02 08:30:00', 'DONE');

INSERT INTO medical_records (appointment_id, diagnosis, notes) VALUES
(1, 'Hypertension', 'Patient needs regular monitoring'),
(3, 'Common cold', 'Rest and drink fluids');

INSERT INTO medicines (name, price, stock_quantity) VALUES
('Paracetamol', 5000, 100),
('Aspirin', 7000, 50),
('Amoxicillin', 12000, 30);

INSERT INTO prescriptions (record_id) VALUES
(1),
(2),
(3);

INSERT INTO prescription_details (prescription_id, medicine_id, quantity, dosage) VALUES
(1, 1, 10, '2 times/day'),
(1, 2, 5, '1 time/day'),
(2, 3, 7, '3 times/day'),
(3, 1, 5, '2 times/day');

INSERT INTO bills (patient_id, total_amount, status) VALUES
(1, 150000, 'PAID'),
(3, 50000, 'UNPAID');

INSERT INTO bill_medicine_details (bill_id, medicine_id, quantity, price) VALUES
(1, 1, 10, 5000),
(1, 2, 5, 7000),
(2, 1, 5, 5000);

