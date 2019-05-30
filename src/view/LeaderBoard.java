package view;

import java.io.Serializable;

/**
 * Save the top 5 players. It can check and save top 5 player information.
 * 
 * @author Chen Wang
 *
 */

public class LeaderBoard implements Serializable {

	private static final long serialVersionUID = 1L;
	int[] coins = { 0, 0, 0, 0, 0 };
	String[] name = { " ", " ", " ", " ", " " };
	int mark = 0;

	public LeaderBoard() {

	}

	/**
	 * Checking whether the coins of player get in top 5.
	 * 
	 * @param getcoins
	 *            the coins winner player get
	 * @return boolean if true player is top 5.
	 */
	public boolean top5(int getcoins) {
		for (int i = 0; i < name.length; i++) {
			if (name[i] == " ") {
				mark = i;
				return true;
			}
			if (coins[i] <= getcoins) {
				mark = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * save player information in top 5 leader board
	 * 
	 * @param user
	 *            player name
	 * @param getcoins
	 *            the coin player get
	 */
	public void settop5(String user, int getcoins) {
		String temp1n;
		if (user.length() > 10) {
			temp1n = user.substring(0, 9);
		} else {
			temp1n = user;
		}
		int temp1c = getcoins;
		String temp2n;
		int temp2c;
		for (int i = mark; i < name.length; i++) {
			temp2n = name[i];
			temp2c = coins[i];
			name[i] = temp1n;
			coins[i] = temp1c;
			temp1n = temp2n;
			temp1c = temp2c;
		}
	}

	public String[] getname() {
		return name;
	}

	public int[] getcoin() {
		return coins;
	}
}
