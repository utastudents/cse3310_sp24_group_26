package com.cse3310;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class UserTest {
    User user1 = new User("user1");
    User user2 = new User("user2");
    Game game = new Game();
    ArrayList<User> users = new ArrayList<>();
    /**
     * no duplicate usernames
     */
    public boolean findUsernamesInArray(ArrayList<User> users, String username1, String username2)
    {
        boolean foundUser1 = false;
        boolean foundUser2 = false;
        for(User user:users)
        {
            if(user.getName().equals(username1))
            {
                foundUser1 = true;
            }
            if(user.getName().equals(username2))
            {
                foundUser2 = true;
            }
        }
        return foundUser1 && foundUser2;
    }

    public boolean areUsernamesEqual(ArrayList<User> users, User user1, User user2)
    {
        return users.contains(user1) && users.contains(user2);
    }

    @Test
    public void testForDuplicateUsernames()
    {
        users.add(user1);
        users.add(user2);
        assertTrue(areUsernamesEqual(users, user1, user2));
    }
}   
