package com.testtask.socialnetworkservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments;
}
