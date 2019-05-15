package com.johnson.bid;

import com.johnson.bid.data.User;
import com.johnson.bid.util.UserManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatRoomUnitTest {

    private User mockUser;

    @Before
    public void setUp() {

        mockUser = new User();
        mockUser.getChatList().add(2361838020534541L);

        UserManager.getInstance().setUser(mockUser);
    }

    @Test
    public void hasChatRoom_exist() {
        long id = 2361838020534541L;
        assertEquals(UserManager.getInstance().hasChatRoom(id), true);
    }

    @Test
    public void hasChatRoom_not_exist() {
        long id = 1231231231231231L;
        assertEquals(UserManager.getInstance().hasChatRoom(id), false);
    }

    @Test
    public void hasChatRoom_impossible() {
        long id = -1;
        assertEquals(UserManager.getInstance().hasChatRoom(id), false);
    }
}
