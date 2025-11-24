óli var að vinna í þessu file, pls everybody confirm að við erum game her type script:

kleppir = daníel snær :^)

skogabjorn = ragnar björn :D

Amaragance = ólafur sær xD

Egill = Egill ( ˶°ㅁ°)!!

Buff Slayer

hallo frá ragnari!!

Daníel mættur á svæðið!



doc skjal fyrir skilaverkefni 1:
https://docs.google.com/document/d/14TPepnAFvG9aex2UdFMM2mAFnQ4oLKxGJmchXJklG7M/edit?tab=t.0




git was being stupid so sorry for one big dump but anyways, assignment 2 contribution distribution is like this:

ragnar:
- Login og user functionality
- Security handling and checks
- JWT stuff
- ShiftService og TaskService mostly

egill:
- db design and architecture
- endpoints so far and basic controller setup

daniel:
- Entities and repos according to db (boilerplate stuff eins og mappers)

oli:
- rest of services (not user-related and shiftservice/taskservice)

dtos were just made in cooperation


# Notes for assignment 3

On schedule, most service layer functions are complete, only needing some endpoints for edit requests and obscure alternative scenarios.
That means UC1, UC2, UC3, UC4, UC5, UC6, UC7, UC9 and UC11

Last use cases and testing + fine-tuning will be handled next sprint, and we expect to be able to handle that quite well, seeing the progress made this sprint.

Ragnar has handled code aggregation for the team and have made sure the code is in accordance with the standards we set for outselves, being the P.O. for this sprint.

The project plan has been slightly altered, now including UC11 for user registration and login, seeing as that was missing and only mentioned in a context where it was implied to exist. It can now be accessed in "Project Plan.pdf" in the root folder

# Notes for assignment 4

On schedule, the only thing left to do is to finish UC10, which is an 
easy implementation and should be done before the presentation. 
Functionality for dummy data has been added and advanced shift patching 
and edit request handling has also been completed. For the last week, 
finalizing is the only thing left and stray errors or oversights will 
be ironed out.

UC8 has been removed from the plan, since it did not align with the 
goal of the project in terms of the course, as well as being more 
complicated to implement than we are ready to take on. The updated plan 
can be accessed in Project Plan in the top level of the repository.

# Notes for assignment 5

As per the assignment, here is a rough outline of how to use the program. 
Users and Companies are top level data which have no foreign keys, and 
companies are created by ADMIN users, and users are created by 
registering at /auth/register. Companies and Users can then be tied 
together via a Contract.

At each company, there can exist Locations, and Tasks, where each task 
must be bound to a Location. Then, a shift can be created and bound to 
a contract, where the shift has three sub-classes; shift_breaks, 
shift_tasks and shift_notes. ShiftBreaks are simple classes to denote 
if an employee is taking a break and ShiftTasks shows the task the 
employee was/is working on throughout the shift, and can be multiple 
sections. ShiftNotes is a simple text table which stores miscellaneous 
information.

Employees can then submit edit requests for their shifts and have 
managers review them and apply/deny. Every change in the database is 
also versioned with an audit_log showing the before/after row, who 
conducted the change and at what time.

All endpoints are

| Method | Path                                   | Roles          | DTO                      | Query Params |
| ------ | -------------------------------------- | -------------- | ------------------------ | ------------ |
| GET    | /companies                             | ADMIN          | –                        | –            |
| GET    | /companies/me                          | Authenticated  | –                        | –            |
| GET    | /companies/{companyId}                 | ADMIN, MANAGER | –                        | –            |
| POST   | /companies                             | ADMIN          | –                        | –            |
| PATCH  | /companies/{companyId}                 | ADMIN          | `CompanyPatchRequestDTO` | –            |
| POST   | /companies/{companyId}/add/{userId}    | ADMIN, MANAGER | `ContractRequestDTO`     | –            |
| DELETE | /companies/{companyId}/resign          | Authenticated  | –                        | –            |
| DELETE | /companies/{companyId}/resign/{userId} | ADMIN, MANAGER | –                        | –            |

| Method | Path                                         | Roles             | DTO                     | Query Params                                      |
| ------ | -------------------------------------------- | ----------------- | ----------------------- | ------------------------------------------------- |
| GET    | /companies/{companyId}/employee/locations    | EMPLOYEE, MANAGER | –                       | `name`, `address`                                 |
| GET    | /companies/{companyId}/employee/tasks        | EMPLOYEE, MANAGER | –                       | `locationId`, `name`, `description`, `isFinished` |
| GET    | /companies/{companyId}/employee/shifts       | EMPLOYEE, MANAGER | –                       | `from`, `to`                                      |
| POST   | /companies/{companyId}/employee/edit-request | EMPLOYEE, MANAGER | `EditRequestRequestDTO` | –                                                 |
| POST   | /companies/{companyId}/employee/clock-in     | EMPLOYEE, MANAGER | `ClockInDTO`            | –                                                 |
| POST   | /companies/{companyId}/employee/clock-out    | EMPLOYEE, MANAGER | –                       | –                                                 |
| POST   | /companies/{companyId}/employee/switch-task  | EMPLOYEE, MANAGER | `SwitchTaskRequestDTO`  | –                                                 |
| POST   | /companies/{companyId}/employee/start-break  | EMPLOYEE, MANAGER | `BreakRequestDTO`       | –                                                 |
| POST   | /companies/{companyId}/employee/end-break    | EMPLOYEE, MANAGER | –                       | –                                                 |
    
| Method | Path                                                               | Roles          | DTO                          | Query Params                                                                                  |
| ------ | ------------------------------------------------------------------ | -------------- | ---------------------------- | --------------------------------------------------------------------------------------------- |
| GET    | /companies/{companyId}/manager/shifts                              | ADMIN, MANAGER | –                            | `contractId`, `userId`, `from`, `to`, `isOngoing`                                             |
| GET    | /companies/{companyId}/manager/tasks                               | ADMIN, MANAGER | –                            | `locationId`, `name`, `description`, `isFinished`                                             |
| GET    | /companies/{companyId}/manager/locations                           | ADMIN, MANAGER | –                            | `name`, `address`                                                                             |
| GET    | /companies/{companyId}/manager/edit-requests                       | ADMIN, MANAGER | –                            | `userId`, `shiftId`, `reason`, `status`, `reviewedByUserId`, `reviewedAtFrom`, `reviewedAtTo` |
| GET    | /companies/{companyId}/manager/edit-requests/{editRequestId}       | ADMIN, MANAGER | –                            | –                                                                                             |
| POST   | /companies/{companyId}/manager/tasks                               | ADMIN, MANAGER | `TaskRequestDTO`             | –                                                                                             |
| POST   | /companies/{companyId}/manager/locations                           | ADMIN, MANAGER | `LocationRequestDTO`         | –                                                                                             |
| POST   | /companies/{companyId}/manager/edit-requests/{editRequestId}/apply | ADMIN, MANAGER | –                            | –                                                                                             |
| PATCH  | /companies/{companyId}/manager/tasks/{taskId}                      | ADMIN, MANAGER | `TaskPatchRequestDTO`        | –                                                                                             |
| PATCH  | /companies/{companyId}/manager/locations/{locationId}              | ADMIN, MANAGER | `LocationPatchRequestDTO`    | –                                                                                             |
| PATCH  | /companies/{companyId}/manager/edit-requests/{editRequestId}       | ADMIN, MANAGER | `EditRequestPatchRequestDTO` | –                                                                                             |
| PATCH  | /companies/{companyId}/manager/shifts/{shiftId}                    | ADMIN, MANAGER | `ShiftPatchRequestDTO`       | –                                                                                             |
| DELETE | /companies/{companyId}/manager/tasks/{taskId}                      | ADMIN, MANAGER | –                            | –                                                                                             |
| DELETE | /companies/{companyId}/manager/locations/{locationId}              | ADMIN, MANAGER | –                            | –                                                                                             |
    
| Method | Path                                 | Roles          | DTO | Query Params |
| ------ | ------------------------------------ | -------------- | --- | ------------ |
| GET    | /companies/{companyId}/manager/audit | ADMIN, MANAGER | –   | `entityType` |

| Method | Path           | Roles         | DTO                  | Query Params |
| ------ | -------------- | ------------- | -------------------- | ------------ |
| POST   | /auth/register | –             | `RegisterRequestDTO` | –            |
| POST   | /auth/login    | –             | `LoginRequestDTO`    | –            |
| PUT    | /auth/me       | Authenticated | –                    | –            |
| DELETE | /auth/me       | Authenticated | `UserPutRequestDTO`  | –            |

| Method | Path | Roles | DTO | Query Params |
| ------ | ---- | ----- | --- | ------------ |
| GET    | /all | ADMIN | –   | –            |

The presentation slides can be found in the top level of the repository 
by "Hugbó lokakynning.pdf".

The project is being hosted on render here: [https://hugbunadarverkefni1-snpf.onrender.com](https://hugbunadarverkefni1-snpf.onrender.com)

And the database can be connected to via this information:

postgresql://hugbo1_db_new_user:7Qg4sK1kOIjzT4aKb5BjWNvgQpwpfWHC@dpg-d4dofem3jp1c73cf4dn0-a.frankfurt-postgres.render.com/hugbo1_db_new

