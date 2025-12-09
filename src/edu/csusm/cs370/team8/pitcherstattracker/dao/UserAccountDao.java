package edu.csusm.cs370.team8.pitcherstattracker.dao;

import edu.csusm.cs370.team8.pitcherstattracker.MainWindow;
import edu.csusm.cs370.team8.pitcherstattracker.dto.DtoMapper;
import edu.csusm.cs370.team8.pitcherstattracker.dto.UserAccountDto;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/* DAO for loading and saving the coach's UserAccount.
 */
public interface UserAccountDao {

    UserAccount loadOrCreateDemo();

    void save(UserAccount account);

    /* File-based implementation using a single JSON DTO file.
     */
    final class FileBased implements UserAccountDao {
        private final Path file;

        // One ObjectMapper for this DAO
        private static final ObjectMapper MAPPER = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        public FileBased(Path file) {
            this.file = file;
        }

        @Override
        public UserAccount loadOrCreateDemo() {
            try {
                if (Files.exists(file)) {
                    // Read JSON -> DTO -> domain
                    UserAccountDto dto = MAPPER.readValue(
                            Files.newInputStream(file),
                            UserAccountDto.class
                    );
                    return DtoMapper.fromDto(dto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Fallback: generate demo data using your existing method
            MainWindow.displayError("Account not found in files", "Account JSON file not found, generating a new random one.");
            UserAccount account = UserAccount.generateCoach();
            save(account);
            return account;
        }

        @Override
        public void save(UserAccount account) {
            try {
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }

                UserAccountDto dto = DtoMapper.toDto(account);

                // DTO -> JSON
                MAPPER.writerWithDefaultPrettyPrinter()
                        .writeValue(Files.newOutputStream(file), dto);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
