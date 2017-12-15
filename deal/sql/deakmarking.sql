BEGIN


    DECLARE t_error INTEGER DEFAULT 0;

    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET t_error=1;


		set @trade_fee = amount - amount4;

    START TRANSACTION;

		set @buy_fee = fees1;

		set @p_buy_id = 0;
		set @p_sell_id = 0;
		select buy_id, sell_id INTO @p_buy_id, @p_sell_id FROM market WHERE id = fvifid;

    update orders set success_amount = amount1, left_count = count1, status = status1, update_time = time1, version = version + 1, leftfees = leftfees - @buy_fee where fId = fid1 AND status <> 4 AND status <> 3;
		set @up1 = ROW_COUNT();
		update orders set success_amount = amount2, left_count = count2, status = status2, update_time = time2, version = version + 1, leftfees = leftfees - @trade_fee where fId = fid2 AND status <> 4 AND status <> 3;
		set @up2 = ROW_COUNT();

		insert into oeders_log (orders_id, amount, create_time, prize, count, is_active, market_id, type, version, fees) values (fid1,amount,time1,price,count,isactive1,fvifid,isactive2,0,@buy_fee);
		set @up3 = ROW_COUNT();

    update wallet set total = total + amount3, frozen = frozen - amount - amount3, update_time = time1, version = version + 1 where user_id = uid1 and coin_id = @p_buy_id and frozen + 0.00002 >= amount + amount3;
    set @up4 = ROW_COUNT();
    update wallet set total = total + amount4, update_time = time2, version = version + 1 where user_id = uid2 and coin_id = @p_buy_id;
    set @up5 = ROW_COUNT();

		update wallet set total = total + count - @buy_fee, update_time = time1, version = version + 1 where user_id = uid1 and coin_id = @sell_id;
		set @up6 = ROW_COUNT();
		update wallet set frozen = frozen - count, update_time = time2, version = version + 1 where user_id = uid2 and coin_id = sell_id and frozen + 0.00002 >= count;
		set @up7 = ROW_COUNT();


    IF t_error = 1 or @up1 != 1 or @up2 != 1 or @up3 != 2 or @up4 != 1 or @up5 != 1 or @up6 != 1 or @up7 != 1 THEN
      ROLLBACK;
    ELSE
      COMMIT;
    END IF;

		SELECT @up1, @up2, @up3, @up4, @up5, @up6, @up7;

  END