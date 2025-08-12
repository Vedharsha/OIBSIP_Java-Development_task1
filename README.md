# OIBSIP_Java-Development_Task1

## Project Title  
**Online Reservation System** – A centralized train ticket booking and cancellation system with real-time access for authorized users.

---

## Objective  
To develop a secure and efficient train ticket reservation and cancellation system that allows authenticated users to book or cancel tickets with real-time updates.

---

## Modules  
1. **Login Form** – Secure login for users with ID and password, authenticated with **bcrypt encryption**.  
2. **Reservation System** – Enter passenger details, select train, journey date, and class type.  
3. **Cancellation Form** – Cancel tickets using PNR number.

---

## Tools & Technologies  
- **Programming Language:** Java  
- **Framework:** Spring Boot  
- **Database:** PostgreSQL  
- **Frontend:** HTML, CSS, JavaScript  
- **ORM:** Spring Data JPA  

---

## Steps Performed  
1. Designed database schema for users, reservations, and cancellations.  
2. Implemented backend APIs using Spring Boot.  
3. Integrated database with Spring Data JPA.  
4. Provided secure authentication using a one-way hashing technique (**bcrypt**).  
5. Booking and cancellation seat counts are updated using a **scheduler** instead of direct database modification.  
6. Tested each module for proper functioning.  

---

## Outcome  
- Users can securely log in, reserve train tickets, and cancel them using a valid PNR.  
- Centralized database ensures quick access and management of reservation data.  
- Seat count management is automated for accuracy.

---

## LinkedIn Post  
