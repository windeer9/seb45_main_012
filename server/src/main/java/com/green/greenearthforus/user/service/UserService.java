package com.green.greenearthforus.user.service;
import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.image.service.ImageService;
import com.green.greenearthforus.post.repository.PostRepository;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.repository.UserRepository;
import com.green.greenearthforus.user.dto.UserPatchDto;
import com.green.greenearthforus.user.dto.UserPostDto;
import com.green.greenearthforus.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.green.greenearthforus.user.entity.User.Role.USER;
import static com.green.greenearthforus.user.entity.User.UserGrade.LAND;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository,
                       UserMapper mapper,
                       ImageService imageService,
                       PasswordEncoder passwordEncoder,
                       PostRepository postRepository){
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }


    // 사용자 등록
    public User createUser(UserPostDto userPostDto, MultipartFile image) {

        User user = mapper.userPostDtoToUser(userPostDto);

        Optional<User> checkUser = userRepository.findAll().stream()
                .filter(finderuser -> finderuser.getUserUseId().equals(user.getUserUseId()))
                .findFirst();
        if(checkUser.isPresent()) throw new BusinessLogicException(ExceptionCode.USER_EXISTS);

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);//password 인코딩 후 저장.
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(USER);
        user.setGrade(LAND);
        user.setImageUrl(imageUpload(image));

        return userRepository.save(user);
    }

    // 사용자 조회
    public User getUser(Long userId) {


        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

    }

    // 해당 사용자 질문 확인
    public boolean verifyAnswer(Long userId, String answer) {

        User user = getUser(userId);
        return user.getPasswordAnswer().equals(answer);
    }

    // 사용자 수정
    public User updateUser(Long userId, UserPatchDto userPatchDto, MultipartFile image) {

        User existing = getUser(userId);

        Optional.ofNullable(userPatchDto.getUserName()).ifPresent(existing::setUserName);
        Optional.ofNullable(userPatchDto.getPassword()).ifPresent(password -> existing.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(userPatchDto.getPasswordQuestion()).ifPresent(existing::setPasswordQuestion);
        Optional.ofNullable(userPatchDto.getPasswordAnswer()).ifPresent(existing::setPasswordAnswer);
        imageUpload(image);
        updateGradePostCount(existing);


        return userRepository.save(existing);
    }

    // 사용자 삭제
    public void deleteUser(Long userId) {
        User existing = getUser(userId);
        userRepository.delete(existing);
    }
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
//        userRepository.deleteById(63L);
//        List<User> duplicateUsers = entityManager.createQuery(
//                        "SELECT u FROM User u " +
//                                "WHERE u.userId IN (" +
//                                "    SELECT userId FROM User GROUP BY userId HAVING COUNT(userId) > 1" +
//                                ")", User.class)
//                .getResultList();
//       for(User user : duplicateUsers){
//           userRepository.delete(user);
//    }
//        postRepository.deleteById(147L);
//        postRepository.deleteById(148L);
//        userRepository.deleteByUserId(63L);
    }



    public void updateGradePostCount(User user) {

        int postCount = user.getPosts().size(); // 사용자의 게시글 수 가져와서 쌓기 실제로는 사진이 들어감

        User.UserGrade newGrade = calculateUserGrade(postCount);
        user.setGrade(newGrade);

//         등급에 따른 이미지 URL 설정 -> 상태값 던져주면 fe에서 받아서 쓰는것도 ... /
        switch (newGrade) {
            case LAND:
                user.setGradeImageFile("url_for_land_image");
                break;
            case SPROUT:
                user.setGradeImageFile("url_for_sprout_image");
                break;
            case GROWING_SPROUT:
                user.setGradeImageFile("url_for_growing_sprout_image");
                break;
            case BUD:
                user.setGradeImageFile("url_for_bud_image");
                break;
            case FLOWER:
                user.setGradeImageFile("url_for_flower_image");
                break;
            default:
                user.setGradeImageFile("default_image_url");
                break;
        }

    }

    private User.UserGrade calculateUserGrade(int postCount) {
        if (postCount == 0) {
            return LAND;
        } else if (postCount >= 1 && postCount < 5) {
            return User.UserGrade.SPROUT;
        } else if (postCount >= 5 && postCount < 10) {
            return User.UserGrade.GROWING_SPROUT;
        } else if (postCount >= 10 && postCount < 20) {
            return User.UserGrade.BUD;
        } else {
            return User.UserGrade.FLOWER;
        }

    }

    private String imageUpload(MultipartFile image){
        if(image!=null) {
            return imageService.uploadImage(image);
        }else {
            return null;
        }
        }

}
