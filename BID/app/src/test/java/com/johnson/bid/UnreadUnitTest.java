package com.johnson.bid;

import com.johnson.bid.data.User;
import com.johnson.bid.util.UserManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnreadUnitTest {

    private User mockUser;

    @Before
    public void setUp() {

        mockUser = new User();
        UserManager.getInstance().setUser(mockUser);
    }

    @Test
    public void increaseUnreadBought_isCorrect() {
        UserManager.getInstance().increaseUnreadBought();
        assertEquals(mockUser.getUnreadBought(), 1);
    }

    @Test
    public void decreaseUnreadBought_isCorrect() {
        UserManager.getInstance().decreaseUnreadBought();
        assertEquals(mockUser.getUnreadBought(), 0);
    }

//    @Test
//    public void decreaseUnreadBought_impossible() {
//        UserManager.getInstance().decreaseUnreadBought();
//        assertEquals(mockUser.getUnreadBought(), -1);
//    }

    @Test
    public void increaseUnreadSold_isCorrect() {
        UserManager.getInstance().increaseUnreadSold();
        assertEquals(mockUser.getUnreadBought(), 1);
    }

    @Test
    public void decreaseUnreadSold_isCorrect() {
        UserManager.getInstance().decreaseUnreadSold();
        assertEquals(mockUser.getUnreadBought(), 0);
    }

    @Test
    public void increaseUnreadNobodyBid_isCorrect() {
        UserManager.getInstance().increaseUnreadNobodyBid();
        assertEquals(mockUser.getUnreadBought(), 1);
    }

    @Test
    public void decreaseUnreadNobodyBid_isCorrect() {
        UserManager.getInstance().decreaseUnreadNobodyBid();
        assertEquals(mockUser.getUnreadBought(), 0);
    }
}
