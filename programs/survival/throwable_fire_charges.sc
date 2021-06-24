// Throw Fireballs like a blaze by Scott Gudeman (DragonCrafted87)

fireball_cooldown_ticks = 50;

__config() -> {'stay_loaded' -> true, 'scope' -> 'global'};

scoreboard_add('fireballCooldown');

__on_player_uses_item(player, item_tuple, hand) ->
(
	[item, count, nbt] = item_tuple || ['None', 0, null];

    item ~ 'fire_charge' &&
    !scoreboard('fireballCooldown', player) &&
    (
    	player_looking = player ~ 'look';
    	player_position = player ~ 'pos';
    	player_game_mode = player ~ 'gamemode';

    	spawn_position = player_position + player_looking + [0, 1.5, 0];
    	fireball = spawn(
    	    'small_fireball',
    	    spawn_position,
    		str('{power:[%.2f,%.2f,%.2f],direction:[0.0,0.0,0.0]},ExplosionPower:0', player_looking / 25)
    	);
    	sound('item.firecharge.use', spawn_position);

        player_game_mode ~ 'survival' &&
        (
            if( hand ~ 'offhand',
                slot = -1,
                slot = player ~ 'selected_slot'
            );

        	inventory_set(player, slot, count-1);
        	modify(player, 'tag', 'fireball_cooldown');
        	scoreboard('fireballCooldown', player, true);
        	schedule(fireball_cooldown_ticks, '_clear_cooldown', player);
        )
    )
);

_clear_cooldown(player) ->
(
    scoreboard('fireballCooldown', player, false);
);
