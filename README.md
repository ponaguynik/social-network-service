### API description
1. Загрузить пользователей по URL:

    _/users/load, POST_
     
    `{
        "url": "https://jsonplaceholder.typicode.com/users"
    }`

2. Загрузить посты по URL:

    _/posts/load, POST_
     
    `{
        "url": "https://jsonplaceholder.typicode.com/posts"
    }`

3. Загрузить комментарии по URL:
    
    _/comments/load, POST_ 
     
    `{
        "url": "https://jsonplaceholder.typicode.com/comments"
    }`

4. Вернуть пользователя оставившего комментарий под постом:

    _/comments/{commentId}/user, GET_

5. Вернуть все комментарии оставленные пользователем:

    _/comments, GET, params: `userId`_

6. Вывести все посты с указанным title:

    _/posts, GET, params: `title`_

7. Удалить пост со всеми его комментариями:

    _/posts/{postId}, DELETE_

8. Вывести сколько раз встречается слово «error» в body всех комментариев (средствами java 8):

    _/comments/count, POST, params: `wordInBody=error`_