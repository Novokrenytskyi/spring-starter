package com.dmdev.spring.database.entity;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_chat")
public class UserChat implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public void setUser(User user) {
      this.user = user;
      user.getUserChats().add(this);

    }

    public void setChat(Chat chat) {
        this.chat = chat;
        chat.getUserChats().add(this);
    }

}
