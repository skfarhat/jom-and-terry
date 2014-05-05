import static org.junit.Assert.*;
import game.Account;

import org.junit.Test;


public class AccountSaveLoadTest {

	@Test
	public void test() {
		final String testUsername = "TestUsername";
		final boolean isRobber = false;
		
		Account account = new Account(testUsername, isRobber);
		assertTrue(account.create());
		account.setHighestLevelReached(3);
		account.setHighscore(1500);
		
		account.save(300);
		Account loadedAccount = Account.load(testUsername);
		
		assertEquals(account.getHighestLevelReached(), loadedAccount.getHighestLevelReached());
		assertEquals(account.getHighscore(), loadedAccount.getHighscore());

			
		assertTrue(account.delete());
	}

}
