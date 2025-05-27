# Database Directory Structure 
 
This directory contains scripts and tools for managing the database schema, 
seed data, customizations, backups, and restores. 


* **DB creation**
* **Seed data**
* **Customizations**
* **Migrations**
* **Backup and restore**
* **Other DB utilities**

### 📁 Recommended Directory Structure

```
/database
│
├── /schema
│   ├── create.sql              # Raw SQL for initial schema setup (optional if using migrations)
│   ├── indexes.sql             # Optional: separate index definitions
│   └── constraints.sql         # Optional: constraints (FKs, etc.)
│
├── /migrations
│   ├── V001__init.sql          # Versioned migration files (Flyway/Liquibase format recommended)
│   ├── V002__add_users_table.sql
│   └── ...
│
├── /seeds
│   ├── development
│   │   └── seed_users.sql
│   ├── staging
│   │   └── seed_users.sql
│   └── production
│       └── seed_users.sql
│
├── /customizations
│   ├── views/
│   ├── triggers/
│   ├── stored_procedures/
│   └── functions/
│
├── /backup
│   ├── manual/
│   └── scheduled/
│
├── /restore
│   ├── restore_from_backup.sh  # Scripts to restore DB
│   └── latest_restore.sql
│
└── README.md                   # Document DB structure and processes
```

---
