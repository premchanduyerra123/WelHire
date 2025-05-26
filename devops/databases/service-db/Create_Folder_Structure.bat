
@echo off
set "BASE_DIR=database"

echo Creating base directory: %BASE_DIR%
mkdir %BASE_DIR%

echo Creating schema directory...
mkdir %BASE_DIR%\schema

echo Creating migrations directory...
mkdir %BASE_DIR%\migrations

echo Creating seeds directories...
mkdir %BASE_DIR%\seeds
mkdir %BASE_DIR%\seeds\development
mkdir %BASE_DIR%\seeds\staging
mkdir %BASE_DIR%\seeds\production

echo Creating customizations subdirectories...
mkdir %BASE_DIR%\customizations
mkdir %BASE_DIR%\customizations\views
mkdir %BASE_DIR%\customizations\triggers
mkdir %BASE_DIR%\customizations\stored_procedures
mkdir %BASE_DIR%\customizations\functions

echo Creating backup subdirectories...
mkdir %BASE_DIR%\backup
mkdir %BASE_DIR%\backup\manual
mkdir %BASE_DIR%\backup\scheduled

echo Creating restore directory...
mkdir %BASE_DIR%\restore

echo Creating README.md...
echo # Database Directory Structure > %BASE_DIR%\README.md
echo. >> %BASE_DIR%\README.md
echo This directory contains scripts and tools for managing the database schema, >> %BASE_DIR%\README.md
echo seed data, customizations, backups, and restores. >> %BASE_DIR%\README.md

echo All directories created successfully.
pause
