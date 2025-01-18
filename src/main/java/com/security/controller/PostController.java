package com.security.controller;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.security.entity.Post;
import com.security.payload.PostDto;
import com.security.payload.Response;
import com.security.service.impl.PostService;
import org.apache.logging.log4j.LogManager;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private static final Logger logger = LogManager.getLogger(PostController.class);

	@Autowired
	private PostService postService;
	Response response = new Response();

	 @PreAuthorize("hasRole('admin')")
	// create blog post rest api
	@PostMapping
	public Response createPost(@RequestBody PostDto postDto) {
		logger.info("PostController || create Post || called");
		return response = postService.createPost(postDto);
	}

	/*--------------------------QR--------------------------*/
	// http://localhost:8080/Api/generateQR/id
	@GetMapping("/generateQR/{Id}")
	public ResponseEntity<byte[]> generateQRCode(@PathVariable("Id") long id) {
		logger.info("PostController || generateQRCode || called");
		try {
			Response resp = postService.getTaskById(id);
			logger.info("Response: {}", resp);

			if (resp.getRespCode() == 0) {
				logger.info("Inside if condition");
				Post task = (Post) resp.getRespData();
				String additionalData = "Prashant Nandanwar, 2973";

				byte[] qrCode = QrCodeGenerator.generateQRCode(task, additionalData);
				logger.info("Generated QR Code: {}", qrCode);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_PNG);
				headers.setContentLength(qrCode.length);
				return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error generating QR code", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// get all posts rest api
	@GetMapping
	public Response getAllPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		return response = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
	}

	// get post by id
	@GetMapping("/{id}")
	public Response getPostById(@PathVariable(name = "id") long id) {
		logger.info("PostController || getPostById || called");
		return response = postService.getPostById(id);
	}

	// update post by id rest api
	@PutMapping("/{id}")
	public Response updatePost(@RequestBody PostDto postDto, @PathVariable(name = "id") long id) {
		logger.info("PostController || getPostById || called");
		return response = postService.updatePost(postDto, id);
	}

	// delete post rest api
	@DeleteMapping("/{id}")
	public Response deletePost(@PathVariable(name = "id") long id) {
		logger.info("PostController || deletePost || called");
		return response = postService.deletePostById(id);
	}
}