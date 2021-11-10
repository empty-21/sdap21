INSERT INTO
    token_template (
        template_name,
        token_base_id,
        token_behavior_id,
        template_creator,
        updatedat
    )
VALUES
    (
        :#${body[name]},
        :#${body[base]},
        :#${body[behavior]},
        :#${body[creator]},
        CURRENT_TIMESTAMP
    )
ON CONFLICT (template_name, template_creator,token_base_id,token_behavior_id) DO NOTHING;
