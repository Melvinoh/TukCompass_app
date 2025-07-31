package com.project.tukcompass.models

data class PostModel(
    val postID: String,
    val clubSportID: String,
    val posts_tb: PostContent,

    )

data class PostContent(
    val description: String,
    val imageURL: String,
    val date: String,
    val user_tb: UserModels
)

data class  PostResponse(
    val posts: List<PostModel>,
)

data class PostReqData(
    val clubID: String,
    val description: String,
)


data class CommentModel(
    val commentID: String,
    val description: String,
    val user_tb: UserModels,
    )

data class CommentResponse(
    val comments : List<CommentModel>
)
data class CommentRequest(
    val postID: String
)

data class CommentReqData(
    val postID: String,
    val description : String
)
