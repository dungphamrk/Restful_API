package ra.ss14.service.impl;

import ra.ss14.model.entity.RefreshToken;
import ra.ss14.model.entity.User;
import ra.ss14.repository.RefreshTokenRepo;
import ra.ss14.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.ss14.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepository;
    private final JWTProvider jwtProvider;

    public RefreshToken createRefreshToken(User user, String ip) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setAddressIp(ip);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        return refreshTokenRepository.save(token);
    }

    public boolean isValid(RefreshToken token, String ip) {
        return token.getExpiryDate().isAfter(LocalDateTime.now()) && token.getAddressIp().equals(ip);
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void manageRefreshTokenLimit(User user, String ip) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserOrderByExpiryDateAsc(user);
        if (tokens.size() >= 2) {
            refreshTokenRepository.delete(tokens.get(0));
        }
    }

}
