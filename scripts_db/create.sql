DROP TABLE IF EXISTS folders_users;
DROP TABLE IF EXISTS item_tags;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS bookers;
DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS wishlists;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS notifications_sett;
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS priorities;
DROP TABLE IF EXISTS chats;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS folders;
DROP TABLE IF EXISTS relationships;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS status;

-- -----------------------------------------------------
-- Table public.status
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.status (
  id SERIAL NOT NULL,
  status_name VARCHAR(12) NOT NULL,
  
  CONSTRAINT pk_status_id PRIMARY KEY (id),
  CONSTRAINT uk_status_statusName UNIQUE(status_name));
  
INSERT INTO "public"."status" ("id","status_name") VALUES (0 ,'pending');
INSERT INTO "public"."status" ("id","status_name") VALUES (1 ,'accepted');
INSERT INTO "public"."status" ("id","status_name") VALUES (2 ,'declined');
INSERT INTO "public"."status" ("id","status_name") VALUES (3 ,'blocked');


-- -----------------------------------------------------
-- Table public.users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.users (
  id SERIAL NOT NULL,
  login VARCHAR(64) NOT NULL,
  name VARCHAR(64) NOT NULL,
  surname VARCHAR(64) NOT NULL,
  email VARCHAR(320) NOT NULL, --64 characters for the "local part" (username). 1 character for the @ symbol. 255 characters for the domain name.
  password VARCHAR(128) NOT NULL,
  birth DATE NULL,
  phone VARCHAR(16) NULL,
  sex BOOLEAN NULL,
  image VARCHAR(2083) NULL,
  is_active BOOLEAN NOT NULL DEFAULT FALSE,
  reg_date TIMESTAMP with time zone NOT NULL DEFAULT current_timestamp,
  conf_link VARCHAR(2083) NULL,
  
  CONSTRAINT pk_users_id PRIMARY KEY (id),
  CONSTRAINT uk_users_login UNIQUE (login),
  CONSTRAINT uk_users_email UNIQUE (email),
  CONSTRAINT uk_users_phone UNIQUE (phone)

	);
-- -----------------------------------------------------
-- Table public.settings
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.settings (
  user_id INT NOT NULL,
  personal_plan_notification BOOLEAN NOT NULL DEFAULT FALSE,
  from_date DATE NULL,
  plan_period INT NULL,
  notification_period INT NULL,
  
  CONSTRAINT pk_settings_id PRIMARY KEY (user_id),
  CONSTRAINT fk_settings_users FOREIGN KEY (user_id)
	REFERENCES public.users (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table public.relationships
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.relationships (
  user_one_id INT NOT NULL,
  user_two_id INT NOT NULL,
  status_id INT NOT NULL,
  action_user_id INT NOT NULL,
  
  CONSTRAINT pk_relationships_usersId PRIMARY KEY (user_one_id, user_two_id),
  
  CONSTRAINT fk_relationships_user1 FOREIGN KEY (user_one_id)
    REFERENCES public.users (id),
    
  CONSTRAINT fk_relationships_user2 FOREIGN KEY (user_two_id)
    REFERENCES public.users (id),
    
  CONSTRAINT fk_relationships_userAct FOREIGN KEY (action_user_id)
    REFERENCES public.users (id),
    
  CONSTRAINT fk_relationships_status FOREIGN KEY (status_id)
    REFERENCES public.status (id)
        );


-- -----------------------------------------------------
-- Table public.folders
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.folders (
  id SERIAL NOT NULL,
  folder_name VARCHAR(45) NOT NULL,

  CONSTRAINT pk_folders_id PRIMARY KEY (id));

-- -----------------------------------------------------
-- Table public.events
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.events (
  id SERIAL NOT NULL,
  creator_id INT NOT NULL, 
  folder_id INT NULL,
  name VARCHAR(60) NOT NULL,
  description TEXT NOT NULL,
  place VARCHAR(60) NULL, -- type for coordrinates mb will change
  timeline_start TIMESTAMP NULL,
  timeline_finish TIMESTAMP NULL,
  period_in_days INT NULL,
  image VARCHAR(2083) NULL, --image link
  is_sent BOOLEAN NOT NULL DEFAULT FALSE,
  is_private BOOLEAN NOT NULL DEFAULT FALSE,
  
  CONSTRAINT pk_events_id PRIMARY KEY (id),

  CONSTRAINT fk_events_folder FOREIGN KEY (folder_id)
    REFERENCES public.folders (id),
    
  CONSTRAINT fk_events_creator FOREIGN KEY (creator_id)
    REFERENCES public.users (id)
);
-- -----------------------------------------------------
-- Table public.chats
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS public.chats (
  id SERIAL NOT NULL,
  event_id INT NOT NULL,
  with_creator BOOLEAN NOT NULL,
  image VARCHAR(2083) NULL, --link

  CONSTRAINT pk_chats_id PRIMARY KEY (id),

  CONSTRAINT uk_chats_eventId_withCreator UNIQUE (event_id, with_creator),
 
  CONSTRAINT fk_chats_event FOREIGN KEY (event_id)
	REFERENCES public.events (id)
    );


-- -----------------------------------------------------
-- Table public.priorities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.priorities (
  id SERIAL NOT NULL,
  priority VARCHAR(10) NOT NULL,
  
  CONSTRAINT pk_priorities_id PRIMARY KEY (id),
  CONSTRAINT uk_priorities_priority UNIQUE (priority)
  );

-- -----------------------------------------------------
-- Table public.participants
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.participants (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  event_id INT NOT NULL,
  priority_id INT NOT NULL,
  
  
  CONSTRAINT pk_participatns_id PRIMARY KEY (id),
 
  CONSTRAINT fk_participants_user FOREIGN KEY (user_id)
    REFERENCES public.users (id),
    
  CONSTRAINT fk_participants_event FOREIGN KEY (event_id)
    REFERENCES public.events (id),
    
  CONSTRAINT fk_participants_priority FOREIGN KEY (priority_id)
    REFERENCES public.priorities (id)
  );

 -- -----------------------------------------------------
-- Table public.notifications_sett
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.notifications_sett (
  participant_id INT NOT NULL,
  count_down_on BOOLEAN NOT NULL DEFAULT FALSE,
  period INT NULL,
  start_date DATE NULL,
  email_notif_on BOOLEAN NOT NULL DEFAULT FALSE,
  bell_notif_on BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT pk_notificationSett_id PRIMARY KEY (participant_id),

  CONSTRAINT fk_notificationSett_participantId FOREIGN KEY (participant_id)
    REFERENCES participants (id)
);


  
-- -----------------------------------------------------
-- Table public.messages
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.messages (
  id SERIAL NOT NULL,
  chat_id INT NOT NULL,
  date TIMESTAMP NOT NULL DEFAULT current_timestamp,
  participant_id INT NOT NULL,
  text VARCHAR(2083) NOT NULL, --link on file with formated text
  
  CONSTRAINT pk_messages_id PRIMARY KEY (id),

  CONSTRAINT fk_messages_chat FOREIGN KEY (chat_id)
    REFERENCES public.chats (id),
    
  CONSTRAINT fk_messages_participant FOREIGN KEY (participant_id)
    REFERENCES public.participants (id)
  );


-- -----------------------------------------------------
-- Table public.wishlists
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.wishlists (
  id SERIAL NOT NULL,
  name VARCHAR(45) NULL,
  user_id INT NOT NULL,
  
  CONSTRAINT pk_wishlists_id PRIMARY KEY (id),
  
  CONSTRAINT uk_wishlists_user_id UNIQUE (user_id),
  
  CONSTRAINT fk_wishlists_user FOREIGN KEY (user_id)
	REFERENCES public.users (id)
  );


-- -----------------------------------------------------
-- Table public.items
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.items (
  id SERIAL NOT NULL,
  name VARCHAR(45) NOT NULL,
  priority_id INT NOT NULL,
  description VARCHAR(2083) NULL, --link
  wishlist_id INT NOT NULL,
  
  CONSTRAINT pk_items_id PRIMARY KEY (id),
  
  CONSTRAINT fk_items_priority FOREIGN KEY (priority_id)
    REFERENCES public.priorities (id),
    
  CONSTRAINT fk_items_wishlists FOREIGN KEY (wishlist_id)
    REFERENCES public.wishlists (id)
  );

-- -----------------------------------------------------
-- Table public.bookers
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.bookers (
  item_id INT NOT NULL,
  participant_id INT NOT NULL,
  
  CONSTRAINT pk_bookers_itemId_participantId PRIMARY KEY (item_id, participant_id),
  
  CONSTRAINT fk_bookers_item FOREIGN KEY (item_id)
    REFERENCES public.items (id),
    
  CONSTRAINT fk_bookers_participant FOREIGN KEY (participant_id)
    REFERENCES public.participants (id)
  );


-- -----------------------------------------------------
-- Table public.tags
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.tags (
  id SERIAL NOT NULL,
  tag_name VARCHAR(30) NOT NULL,
  
  CONSTRAINT pk_tags_id PRIMARY KEY (id),
  CONSTRAINT uk_tags_tag_name UNIQUE (tag_name));


-- -----------------------------------------------------
-- Table public.likes
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.likes (
  user_id INT NOT NULL,
  item_id INT NOT NULL,
  
  CONSTRAINT pk_likes_userId_itemId PRIMARY KEY (user_id, item_id),
 
  CONSTRAINT fk_likes_user FOREIGN KEY (user_id)
    REFERENCES public.users (id), 
    
  CONSTRAINT fk_likes_item FOREIGN KEY (item_id)
    REFERENCES public.items (id)
  );

-- -----------------------------------------------------
-- Table public.item_tags
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.item_tags (
  item_id INT NOT NULL,
  tag_id INT NOT NULL,
  
  CONSTRAINT pk_item_tag_itemId_tagId PRIMARY KEY (item_id, tag_id),
  
  CONSTRAINT fk_item_tags_item FOREIGN KEY (item_id)
    REFERENCES public.items (id),
    
  CONSTRAINT fk_item_tags_tag FOREIGN KEY (tag_id)
    REFERENCES public.tags (id)
  );


-- -----------------------------------------------------
-- Table public.images
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.images (
  id SERIAL NOT NULL,
  image VARCHAR(2083) NOT NULL,
  item_id INT NULL,
  message_id INT NULL,

  CONSTRAINT pk_item_images_itemId_imageId PRIMARY KEY (id),
 
  CONSTRAINT fk_item_images_item FOREIGN KEY (item_id)
    REFERENCES public.items (id),

  CONSTRAINT fk_message_images_message FOREIGN KEY (message_id)
    REFERENCES public.messages (id)
  );
  
-- -----------------------------------------------------
-- Table public.folders_users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS public.folders_users (
  user_id INT NOT NULL,
  folder_id INT NOT NULL,
  is_creator BOOLEAN,
  
  CONSTRAINT pk_folders_users_folderId_userId PRIMARY KEY (user_id, folder_id),
  
  CONSTRAINT fk_folders_users_users FOREIGN KEY (user_id)
    REFERENCES public.users (id),
    
  CONSTRAINT fk_folders_users_folders FOREIGN KEY (folder_id)
    REFERENCES public.folders (id)
  );