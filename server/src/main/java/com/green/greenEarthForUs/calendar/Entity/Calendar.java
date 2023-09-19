package com.green.greenEarthForUs.calendar.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long calendarId;

    @Column
    private String body;

    @Column
    private LocalDateTime localDateTime;

//    @OneToMany
//    public Post post;
//
//    @ManyToOne
//    public User user;

}
