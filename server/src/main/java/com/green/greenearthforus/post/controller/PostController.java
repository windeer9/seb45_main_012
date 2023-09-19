package com.green.greenearthforus.post.controller;

import com.green.greenearthforus.exception.ImageDeletionException;
import com.green.greenearthforus.image.service.ImageService;
import com.green.greenearthforus.post.dto.PostPatchDto;
import com.green.greenearthforus.post.dto.PostPostDto;
import com.green.greenearthforus.post.dto.PostResponseDto;
import com.green.greenearthforus.post.entity.Post;
import com.green.greenearthforus.post.mapper.PostMapper;
import com.green.greenearthforus.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/post")
public class PostController {

    final PostService postService;
    private final PostMapper mapper;

    @Autowired
    public PostController(PostService postService, PostMapper mapper, ImageService imageService) {
        this.postService = postService;
        this.mapper = mapper;
    }

    // 게시글 생성
    @PostMapping("/{user-id}")
    public ResponseEntity<PostResponseDto> createPost(@PathVariable(value = "user-id") Long userId,
                                                      @RequestPart(value = "image", required = false) MultipartFile image,
                                                      @RequestPart(value = "json") PostPostDto postPostDto) {

        Post createdPost = postService.createPost(userId, postPostDto, image);

        PostResponseDto responseDto = mapper.postToPostResponseDto(createdPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 단일 게시글 조회
    @GetMapping("/{post-id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable(value="post-id") Long postId) {
        PostResponseDto responseDto = postService.getPost(postId);

        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 모든 게시글 조회
    @GetMapping("/all")
    public ResponseEntity<List<PostResponseDto>> getPostAll() {
        List<PostResponseDto> postList = postService.getAllPosts();
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    // 게시판 별 게시글 조회
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PostResponseDto>> getPostByType(@PathVariable("type") String type) {
        List<PostResponseDto> postList = postService.getPostsByType(type);
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    // 사용자 별 조회
    @GetMapping("/customer/{user-id}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable(value = "user-id") Long userId) {
        List<PostResponseDto> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    // 게시글 수정
    @PatchMapping("/{user-id}/{post-id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable(value = "user-id") Long userId,
                                                      @PathVariable(value = "post-id") Long postId,
                                                      @RequestPart(value = "image", required = false) MultipartFile image,
                                                      @RequestPart(value = "json") PostPatchDto postPatchDto) {


        PostResponseDto updatedPost = postService.updatePost(userId, postId, postPatchDto, image);


        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{user-id}/{post-id}")
    public ResponseEntity<Void> deletePost(@PathVariable(value = "user-id") Long userId,
                                           @PathVariable(value = "post-id") Long postId) throws ImageDeletionException {
        postService.deletePost(postId, userId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll(){
        postService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}