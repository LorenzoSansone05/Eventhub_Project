INSERT INTO "Role" ("name") VALUES ('ROLE_USER') ON CONFLICT ("name") DO NOTHING;
INSERT INTO "Role" ("name") VALUES ('ROLE_ORGANIZER') ON CONFLICT ("name") DO NOTHING;
INSERT INTO "Role" ("name") VALUES ('ROLE_ADMIN') ON CONFLICT ("name") DO NOTHING;

INSERT INTO "User" ("email", "password", "isBanned", "role_id", "createdAt", "updatedAt")
VALUES (
    'admin@eventhub.com',
    -- Questa è la stringa criptata in BCrypt per la password: "adminpassword"
    '$2a$12$pJFMKmZwaUfvh8xORzcrresYFX7nSHjX3zgEInDaz.BxK69bOovvG',
    false,
    (SELECT "id" FROM "Role" WHERE "name" = 'ROLE_ADMIN'),
    NOW(),
    NOW()
) ON CONFLICT ("email") DO NOTHING;