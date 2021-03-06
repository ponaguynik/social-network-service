package com.testtask.socialnetworkservice.model;

import lombok.*;

import javax.persistence.*;

@Table(indexes = @Index(name = "IDX_COMMENT_EMAIL", columnList = "email"))
@Entity
@Data
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Post post;
    private String name;
    private String email;
    @Column(columnDefinition = "TEXT")
    private String body;
}
