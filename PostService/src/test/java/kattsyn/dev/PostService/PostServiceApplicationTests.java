package kattsyn.dev.PostService;

import io.grpc.stub.StreamObserver;
import kattsyn.dev.PostService.grpc.PostServiceGrpc;
import kattsyn.dev.PostService.grpc.PostServiceOuterClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kattsyn.dev.PostService.grpc.PostServiceImpl;

@SpringBootTest
class PostServiceApplicationTests {

	@Autowired
	PostServiceImpl postService;
	@Test
	void contextLoads() {
	}

	@Test
	void createPostTest() {
		PostServiceOuterClass.CreatePostRequest request = PostServiceOuterClass.CreatePostRequest.newBuilder()
				.setAuthorId(1)
				.setHeader("Very first post")
				.setText("Interesting post text about pandas. :))")
				.build();
		//StreamObserver<PostServiceOuterClass.CreatePostResponse> observer = StreamObserver<PostServiceOuterClass.CreatePostResponse.newBuilder().build()>
		postService.createPost(request, null);
	}

	@Test
	void deletePostTest() {
		PostServiceOuterClass.DeletePostRequest request = PostServiceOuterClass.DeletePostRequest.newBuilder()
				.setPostId(1)
				.build();
		postService.deletePost(request, null);
	}

	@Test
	void notAuthorPostDeleteTry() {
		PostServiceOuterClass.EditPostRequest request = PostServiceOuterClass.EditPostRequest.newBuilder()
				.setPostId(2)
				.setUserId(123)
				.setHeader("New Header set not by author")
				.build();
		postService.editPost(request, null);
	}

	@Test
	void authorPostDeleteTry() {
		PostServiceOuterClass.EditPostRequest request = PostServiceOuterClass.EditPostRequest.newBuilder()
				.setPostId(2)
				.setUserId(1)
				.setHeader("New Header set by author")
				.build();
		postService.editPost(request, null);
	}

	@Test
	void getPostByIdTest() {
		PostServiceOuterClass.GetPostByIdRequest request = PostServiceOuterClass.GetPostByIdRequest.newBuilder()
				.setPostId(2)
				.build();
		postService.getPostById(request, null);
	}

	@Test
	void getPostsWithPaginationTest() {
		PostServiceOuterClass.GetPostsRequest request = PostServiceOuterClass.GetPostsRequest.newBuilder()
				.setPage(0)
				.setCount(5)
				.build();
		postService.getPosts(request, null);
	}

}
