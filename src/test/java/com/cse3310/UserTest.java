package com.cse3310;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class UserTest {
    User user1 = new User("");
    User user2 = new User("");
    //Game game = new Game();
    ArrayList<User> users = new ArrayList<>();
    /**
     * no duplicate usernames
     */
    public boolean usernamesAreNotEqual(ArrayList<User> users, String username1, String username2)
    {
        user1.setName(username1);
        user2.setName(username2);
        if(user1.getName().equals(user2.getName()))
        {
            return false;
        }
        return true;
    }

    @Test
    public void testForDuplicateUsernames()
    {
        assertTrue(usernamesAreNotEqual(users, "user", "user1"));
    }
}   
