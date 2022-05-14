CREATE TABLE AppUsers (
    app_user_id SERIAL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    middle_name VARCHAR DEFAULT '',
    username VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    phone_number CHAR(10) NOT NULL,
    date_of_birthday DATE NOT NULL,
    date_of_registration DATE DEFAULT now() NOT NULL,

    CONSTRAINT app_users_pk PRIMARY KEY (app_user_id),

    CONSTRAINT app_user_username_check CHECK (app_user_id ~ '^w{3,}$'),
    CONSTRAINT app_user_email_check CHECK (email ~ '^\w+@(\w+\.\w+)+$'),
    CONSTRAINT app_user_phone_number_check CHECK (phone_number ~ '^[0-9]{10}$'),
    CONSTRAINT app_user_date_of_birthday_check CHECK (date_of_birthday < now()),
    CONSTRAINT app_user_full_name_check CHECK (
        (
            first_name ~ '^([a-zA-Z]+\-?[a-zA-Z]+)+$' and
            last_name ~ '^([a-zA-Z]+\-?[a-zA-Z]+)+$' and
            middle_name ~ '^[a-zA-Z]*$'
        )
            or
        (
            first_name ~ '^([а-яА-ЯёЁ]+\-?[а-яА-ЯёЁ]+)+$' and
            last_name ~ '^([а-яА-ЯёЁ]+\-?[а-яА-ЯёЁ]+)+$' and
            middle_name ~ '^[а-яА-ЯёЁ]*$'
        )
    ),
    CONSTRAINT app_user_unique_email UNIQUE (email),
    CONSTRAINT app_user_unique_phone_number UNIQUE (phone_number),
    CONSTRAINT app_users_unique_username UNIQUE (username)
);

CREATE TABLE Restaurants (
    restaurant_id SERIAL,
    name VARCHAR NOT NULL,
    location VARCHAR NOT NULL,
    website VARCHAR,
    phone_number CHAR(10),
    email VARCHAR NOT NULL,
    is_active BOOLEAN DEFAULT True NOT NULL,

    CONSTRAINT restaurants_pk PRIMARY KEY (restaurant_id),

    CONSTRAINT restaurants_email_check CHECK (email ~ '^\w+@(\w+\.\w+)+$'),
    CONSTRAINT restaurants_phone_number_check CHECK (phone_number ~ '^[0-9]{10}$'),

    CONSTRAINT restaurants_unique_phone_number UNIQUE (phone_number),
    CONSTRAINT restaurants_unique_name UNIQUE (name),
    CONSTRAINT restaurants_unique_email UNIQUE (email)
);

CREATE TABLE RestaurantUsers (
    restaurant_user_id SERIAL NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    middle_name VARCHAR DEFAULT '',
    phone_number CHAR(10) NOT NULL,
    username VARCHAR NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    email VARCHAR NOT NULL,
    is_active BOOLEAN DEFAULT True NOT NULL,

    CONSTRAINT restaurant_users_pk PRIMARY KEY (restaurant_user_id),

    CONSTRAINT restaurant_users_username_check CHECK (restaurant_user_id ~ '^w{3,}$'),
    CONSTRAINT restaurant_users_email_check CHECK (email ~ '^\w+@(\w+\.\w+)+$'),
    CONSTRAINT restaurant_users_phone_number_check CHECK (phone_number ~ '^[0-9]{10}$'),
    CONSTRAINT restaurant_users_full_name_check CHECK (
        (
            first_name ~ '^([a-zA-Z]+\-?[a-zA-Z]+)+$' and
            last_name ~ '^([a-zA-Z]+\-?[a-zA-Z]+)+$' and
            middle_name ~ '^[a-zA-Z]*$'
        )
            or
        (
            first_name ~ '^([а-яА-ЯёЁ]+\-?[а-яА-ЯёЁ]+)+$' and
            last_name ~ '^([а-яА-ЯёЁ]+\-?[а-яА-ЯёЁ]+)+$' and
            middle_name ~ '^[а-яА-ЯёЁ]*$'
        )
    ),

    CONSTRAINT restaurant_users_unique_email UNIQUE (email),
    CONSTRAINT restaurant_users_unique_phone_number UNIQUE (phone_number)
    CONSTRAINT restaurant_users_unique_username UNIQUE (username)
);

CREATE TABLE RestaurantUsersToRestaurants (
    restaurant_user_id INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,

    CONSTRAINT restaurant_user_fk FOREIGN KEY (restaurant_user_id) REFERENCES RestaurantUsers(restaurant_user_id),
    CONSTRAINT restaurant_fk FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id)
);

CREATE TABLE RestaurantTables (
    table_id SERIAL,
    restaurant_id INTEGER NOT NULL,
    table_num INTEGER NOT NULL,
    max_amount INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT True,

    CONSTRAINT tables_pk PRIMARY KEY (table_id),
    CONSTRAINT restaurant_tables_restaurants_fk FOREIGN KEY (table_id) REFERENCES Restaurants(restaurant_id),

    CONSTRAINT tables_table_num_check CHECK (table_num >= 0),
    CONSTRAINT tables_max_amount_check CHECK (max_amount >= 1)
);

CREATE TABLE Reservations (
    reservation_id SERIAL,
    app_user_id INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    table_id INTEGER,
    start_reservation_time TIMESTAMP NOT NULL,
    end_reservation_time TIMESTAMP NOT NULL,
    deposit INTEGER DEFAULT 0,
    amount_of_guests INTEGER DEFAULT 1,
    approved_by INTEGER,
    approve_time TIMESTAMP,

    CONSTRAINT reservations_pk PRIMARY KEY (reservation_id),
    CONSTRAINT reservations_app_users_fk FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id),
    CONSTRAINT reservations_restaurants_fk FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id),
    CONSTRAINT reservations_users_restaurants_fk FOREIGN KEY (approved_by) REFERENCES RestaurantUsers(restaurant_user_id),
    CONSTRAINT reservations_restaurant_tables_fk FOREIGN KEY (table_id) REFERENCES RestaurantTables(table_id),

    CONSTRAINT reservations_deposit_size_check CHECK (deposit >= 0),
    CONSTRAINT reservations_amount_of_guests_check CHECK (amount_of_guests >= 1)
);

CREATE TABLE Review(
    review_id SERIAL,
    app_user_id INTEGER NOT NULL,
    restaurant_id INTEGER NOT NULL,
    comment TEXT NOT NULL,
    score NUMERIC(2, 0) NOT NULL,

    CONSTRAINT reviews_pk PRIMARY KEY (review_id),
    CONSTRAINT reviews_app_users_fk FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id),
    CONSTRAINT reviews_restaurants_fk FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id),

    CONSTRAINT reviews_score_check CHECK (score BETWEEN 1 AND 10)
);
