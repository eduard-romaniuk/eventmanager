INSERT INTO folders_users (folder_id, user_id) VALUES (12, 2);

SELECT * FROM folders_users

SELECT id, login, folders_users.user_id is not null is_member 
	FROM (SELECT id, login
		FROM users
		WHERE id IN ((SELECT relationships.user_two_id 
				FROM relationships
					INNER JOIN status ON status.id = relationships.status_id 
				WHERE relationships.user_one_id = (SELECT DISTINCT user_id
									FROM folders INNER JOIN folders_users ON folders.id = folders_users.folder_id
									WHERE folders.id = 12 AND folders_users.is_creator = TRUE) 
										AND status.status_name = 'accepted') 
								UNION 
								(SELECT relationships.user_one_id 
								FROM relationships 
									INNER JOIN status ON status.id = relationships.status_id 
								WHERE relationships.user_two_id = (SELECT DISTINCT user_id
													FROM folders INNER JOIN folders_users ON folders.id = folders_users.folder_id
													WHERE folders.id = 12 AND folders_users.is_creator = TRUE) 
														AND status.status_name = 'accepted')) 
				ORDER BY id) freands LEFT JOIN folders_users ON id = folders_users.user_id AND folders_users.folder_id = 12 AND folders_users.is_creator is NULL