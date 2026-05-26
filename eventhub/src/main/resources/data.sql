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

INSERT INTO tag (name) VALUES ('TECH') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('MUSICA') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('SPORT') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('BUSINESS') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('ONLINE') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('ARTE') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('CULTURA') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('FOOD') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('CINEMA') ON CONFLICT (name) DO NOTHING;
INSERT INTO tag (name) VALUES ('CONFERENZA') ON CONFLICT (name) DO NOTHING;