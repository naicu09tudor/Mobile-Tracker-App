package edu.tucn.scd.serverapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Naicu Tudor
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    // Creează un utilizator nou. Dacă utilizatorul există deja, returnează null.
    // Transformă DTO în entitate și salvează utilizatorul în baza de date.
    // Returnează utilizatorul salvat sub formă de DTO.
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if(userRepository.findByUser(userDTO.getUsername()) != null){
            return null;
        }
        User user = userMapper.dtoToUser(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.usertoDto(savedUser);
    }


    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    // Actualizează un utilizator. Transformă DTO în entitate, actualizează în baza de date,
    // și returnează utilizatorul actualizat sub formă de DTO.
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userMapper.dtoToUser(userDTO);
        User updatedUser = userRepository.save(user);
        return userMapper.usertoDto(updatedUser);
    }

    // Obține informații despre utilizator pe baza numelui de utilizator.
    // Dacă utilizatorul nu există, returnează null. Transformă entitatea în DTO pentru a o returna.
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUser(username);
        if(user == null){
            return null;
        }
        return userMapper.usertoDto(user);
    }


//    private String encryptPassword(String password) {
//        return BCrypt.hashpw(password, BCrypt.gensalt());
//    }
}
