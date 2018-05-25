DROP TRIGGER IF EXISTS t_add_settings ON users;
DROP TRIGGER IF EXISTS t_del_settings ON users;
DROP TRIGGER IF EXISTS t_add_notif_settings ON participants;
DROP TRIGGER IF EXISTS t_del_notif_settings ON participants;
DROP TRIGGER IF EXISTS t_create_chats ON events;
DROP TRIGGER IF EXISTS t_delete_chats ON events;
DROP TRIGGER IF EXISTS t_user_reg_date_upd ON users;
DROP TRIGGER IF EXISTS t_image_null_values ON images;
DROP TRIGGER IF EXISTS t_friend_controll ON relationships;
DROP TRIGGER IF EXISTS t_create_default_wish_list ON users;


-- --- -------------------------------
-- Triggers for autoremoving and autocreating users settings
-- --- -------------------------------
CREATE OR REPLACE FUNCTION set_user_settings() RETURNS TRIGGER AS $$

BEGIN
    IF    TG_OP = 'INSERT' THEN
        INSERT INTO public.settings(user_id, personal_plan_notification, from_date, plan_period, notification_period) values (NEW.id, FALSE, current_timestamp, 1, 1);
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        DELETE FROM public.settings
		WHERE user_id = OLD.id;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_add_settings
	AFTER INSERT ON users FOR EACH ROW EXECUTE PROCEDURE set_user_settings();

CREATE TRIGGER t_del_settings
	BEFORE DELETE ON users FOR EACH ROW EXECUTE PROCEDURE set_user_settings();



-- --- -------------------------------
-- Triggers for autoremoving and autocreating participant nottification settings
-- --- -------------------------------
CREATE OR REPLACE FUNCTION set_participants_notifications_sett() RETURNS TRIGGER AS $$

BEGIN
    IF    TG_OP = 'INSERT' THEN
        INSERT INTO public.notifications_sett(participant_id, count_down_on, email_notif_on, bell_notif_on) 
		values (NEW.id, FALSE, FALSE, TRUE);
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        DELETE FROM public.notifications_sett
		WHERE participant_id = OLD.id;
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_add_notif_settings
	AFTER INSERT ON participants FOR EACH ROW EXECUTE PROCEDURE set_participants_notifications_sett();

CREATE TRIGGER t_del_notif_settings
	BEFORE DELETE ON participants FOR EACH ROW EXECUTE PROCEDURE set_participants_notifications_sett();

-- --- -------------------------------
-- Triggers for autocreating chats for events
-- --- -------------------------------
CREATE OR REPLACE FUNCTION create_chats() RETURNS TRIGGER AS $$

BEGIN
    IF    TG_OP = 'INSERT' THEN
    
	IF (NEW.is_sent = TRUE) /*AND (NEW.is_private = FALSE)*/ AND (NEW.timeline_start IS NOT NULL) THEN
		INSERT INTO public.chats (event_id, with_creator) VALUES (NEW.id, TRUE);
		INSERT INTO public.chats (event_id, with_creator) VALUES (NEW.id, FALSE);
	END IF;
        
        RETURN NEW;

    ELSIF TG_OP = 'UPDATE' THEN
	--WHEN event is public, create two chats
	IF (NEW.is_sent = TRUE) AND /*(NEW.is_private = FALSE) AND*/ (NEW.timeline_start IS NOT NULL) THEN
		IF (OLD.is_sent = FALSE)/* OR (OLD.is_private = TRUE)*/ OR (OLD.timeline_start IS NULL) THEN
			INSERT INTO public.chats (event_id, with_creator) VALUES (NEW.id, TRUE);
			INSERT INTO public.chats (event_id, with_creator) VALUES (NEW.id, FALSE);
		END IF;
	--WHEN event was public, but now it isnt, delete two chats
	ELSIF (OLD.is_sent = TRUE) /*AND (OLD.is_private = FALSE)*/ AND (OLD.timeline_start IS NOT NULL) THEN
		IF (NEW.is_sent = FALSE) /*OR (NEW.is_private = TRUE)*/ OR (NEW.timeline_start IS NULL) THEN
			DELETE FROM public.chats WHERE event_id = NEW.id;
		END IF;
	END IF;
    
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
		DELETE FROM public.chats WHERE event_id = OLD.id;
	RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_create_chats
	AFTER INSERT OR UPDATE ON events FOR EACH ROW EXECUTE PROCEDURE create_chats();
CREATE TRIGGER t_delete_chats
	BEFORE DELETE ON events FOR EACH ROW EXECUTE PROCEDURE create_chats();

-- --- -------------------------------
-- Trigger for updating registration date
-- --- -------------------------------
CREATE OR REPLACE FUNCTION upd_reg_date() RETURNS TRIGGER AS $$

BEGIN
    IF    TG_OP = 'UPDATE' THEN
       IF (OLD.is_active = FALSE) AND (NEW.is_active = TRUE) THEN
		NEW.reg_date = current_timestamp;
       END IF;
       RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_user_reg_date_upd
	BEFORE UPDATE ON users FOR EACH ROW EXECUTE PROCEDURE upd_reg_date();

-- --- -------------------------------
-- Trigger for controll null values in images
-- --- -------------------------------
CREATE OR REPLACE FUNCTION controll_null_value() RETURNS TRIGGER AS $$

BEGIN

    IF    TG_OP = 'UPDATE' OR TG_OP = 'INSERT' THEN
       IF ((NEW.item_id IS NULL) AND (NEW.message_id IS NULL)) OR
	  ((NEW.item_id IS NOT NULL) AND (NEW.message_id IS NOT NULL)) THEN
		RAISE EXCEPTION 'ONLY ONE FIELD (ITEM_ID OR MESSAGE_ID) MUST BE NULL!';
       END IF;
       RETURN NEW;
	
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_image_null_values
	BEFORE UPDATE OR INSERT ON images FOR EACH ROW EXECUTE PROCEDURE controll_null_value();
	
-- --- -------------------------------
-- Trigger for adding friends
-- --- -------------------------------
CREATE OR REPLACE FUNCTION friends_controll() RETURNS TRIGGER AS $$
DECLARE
	first_friend	integer;
BEGIN
    IF    TG_OP = 'UPDATE' OR TG_OP = 'INSERT' THEN
       IF (NEW.action_user_id != NEW.user_one_id) AND (NEW.action_user_id != NEW.user_two_id) THEN
       
                RAISE EXCEPTION 'USER ACTION ID MUST BE ONE OF THE FRIENDS ID`S!';
                
       ELSIF (NEW.user_one_id > NEW.user_two_id) THEN
       
		first_friend = NEW.user_two_id;
		NEW.user_two_id = NEW.user_one_id;
		NEW.user_one_id = first_friend;

       END IF;
       RETURN NEW;
	
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_friend_controll
	BEFORE UPDATE OR INSERT ON relationships FOR EACH ROW EXECUTE PROCEDURE friends_controll();


-- --- -------------------------------
-- Triggers for autocreating wish list for user
-- --- -------------------------------
CREATE OR REPLACE FUNCTION create_default_wish_list() RETURNS TRIGGER AS $$

BEGIN
    IF    TG_OP = 'INSERT' THEN
        INSERT INTO public.wishlists(user_id) 
		values (NEW.id);
        RETURN NEW;

    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER t_create_default_wish_list
	AFTER INSERT ON users FOR EACH ROW EXECUTE PROCEDURE create_default_wish_list();

