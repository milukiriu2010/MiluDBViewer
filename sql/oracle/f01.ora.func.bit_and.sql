CREATE OR REPLACE FUNCTION     BIT_AND (
	p_IN1			IN	INTEGER,
	p_IN2			IN	INTEGER)
				RETURN	INTEGER
IS
	v_IN1				INTEGER := NVL(p_IN1, 0);
	v_IN2				INTEGER := NVL(p_IN2, 0);
	v_OUT				INTEGER := 0;
	v_BIT1				INTEGER(1);
	v_BIT2				INTEGER(1);
	v_WAIT				INTEGER := 1;
BEGIN
	WHILE v_IN1 > 0 OR v_IN2 > 0 LOOP
		/* ビット取得。 */
		v_BIT1 := MOD(v_IN1, 2);
		v_BIT2 := MOD(v_IN2, 2);
		/* ビットAND */
		IF v_BIT1 = 1 AND v_BIT2 = 1 THEN
			v_OUT := v_OUT + v_WAIT;
		END IF;
		/* シフト */
		v_IN1 := TRUNC(v_IN1 / 2);
		v_IN2 := TRUNC(v_IN2 / 2);
		v_WAIT := v_WAIT * 2;
	END LOOP;
	RETURN v_OUT;
END;
/
