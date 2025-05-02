package com.security.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.controller.PostController;
import com.security.entity.Post;
import com.security.exception.ResourceNotFoundException;
import com.security.payload.PostDto;
import com.security.payload.PostResponse;
import com.security.payload.Response;
import com.security.repository.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PostService {
	private static final Logger logger = LogManager.getLogger(PostController.class);

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ModelMapper mapper;

	Response response = new Response();

	public Response createPost(PostDto postDto) {
		logger.info("PostService || called");
         String date = getDate();
		// convert DTO to entity
		Post post = mapToEntity(postDto);
		post.setCreationDate(date);
		
		Post newPost = postRepository.save(post);
		// convert entity to DTO
		PostDto postResponse = mapToDTO(newPost);
		response.setRespCode(0);
		response.setRespMsg("Post Created Successfull !");
		response.setRespData(postResponse);
		return response;
	}

	private String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	// convert Entity into DTO
	private PostDto mapToDTO(Post post) {
		PostDto postDto = mapper.map(post, PostDto.class);
		return postDto;
	}

	// convert DTO to entity
	private Post mapToEntity(PostDto postDto) {
		Post post = mapper.map(postDto, Post.class);
		return post;
	}

	public Response getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<Post> posts = postRepository.findAll(pageable);

		// get content for page object
		List<Post> listOfPosts = posts.getContent();

		List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(content);
		postResponse.setPageNo(posts.getNumber());
		postResponse.setPageSize(posts.getSize());
		postResponse.setTotalElements(posts.getTotalElements());
		postResponse.setTotalPages(posts.getTotalPages());
		postResponse.setLast(posts.isLast());
		response.setRespCode(0);
		response.setRespMsg("Success");
		response.setRespData(postResponse);
		return response;

	}

	
	public Response getTaskById(long id) { 
		  logger.info("TaskService||getTaskById||called"); 
		  Optional<Post> taskOptional = postRepository.findById(id); 
		  Response response = new Response(); 
		 
		  if (taskOptional.isPresent()) { 
		   Post task = taskOptional.get(); 
		   response.setRespCode(0); 
		   response.setRespMsg("Success"); 
		   response.setRespData(task); 
		  } else { 
		   response.setRespCode(1); 
		   response.setRespMsg("Failure"); 
		   response.setRespData(null); 
		  } 
		 
		  return response; 
		 
		 } 
	public Response getPostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		PostDto mapToDTO = mapToDTO(post);
		response.setRespCode(0);
		response.setRespMsg("Success");
		response.setRespData(mapToDTO);
		return response;
	}

	public Response updatePost(PostDto postDto, long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());

		Post updatedPost = postRepository.save(post);

		PostDto mapToDTO = mapToDTO(updatedPost);
		response.setRespCode(0);
		response.setRespMsg("Success");
		response.setRespData(mapToDTO);
		return response;
	}

	public Response deletePostById(long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		if (post != null) {
			postRepository.delete(post);
			response.setRespCode(0);
			response.setRespMsg("Record Delete successfull !");
		}
		response.setRespCode(1);
		response.setRespMsg("Record Not Found !");
		return response;
	}

}