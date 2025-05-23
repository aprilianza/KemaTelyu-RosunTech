package com.kematelyu.kematelyu.util;

import com.kematelyu.kematelyu.model.Mahasiswa;
import com.kematelyu.kematelyu.model.Staff;
import com.kematelyu.kematelyu.repository.MahasiswaRepository;
import com.kematelyu.kematelyu.repository.StaffRepository;
import com.kematelyu.kematelyu.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;

/**
 * Dev seeder – 40 Mahasiswa & 15 Staff Bardaks + foto avatar acak.
 * Berjalan sekali saat tabel user masih kosong.
 */
@Component
public class DummySeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final MahasiswaRepository mhsRepo;
    private final StaffRepository staffRepo;

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
    private final Random rand = new Random();

    /* -------- avatar pool (terisi di ctor) -------- */
    private final List<String> avatarPool = new ArrayList<>();

    public DummySeeder(UserRepository userRepo,
            MahasiswaRepository mhsRepo,
            StaffRepository staffRepo) {
        this.userRepo = userRepo;
        this.mhsRepo = mhsRepo;
        this.staffRepo = staffRepo;

        /* scan resources/static/user_image/*.(png|jpg|jpeg|webp) */
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        String[] patterns = {
                "classpath:/static/user_image/*.png",
                "classpath:/static/user_image/*.jpg",
                "classpath:/static/user_image/*.jpeg",
                "classpath:/static/user_image/*.webp"
        };
        try {
            for (String p : patterns) {
                for (Resource res : r.getResources(p)) {
                    String filename = Objects.requireNonNull(res.getFilename());
                    avatarPool.add("user_image/" + filename); // simpan relative path
                }
            }
        } catch (Exception e) {
            System.out.println("Avatar scan error: " + e.getMessage());
        }
        if (avatarPool.isEmpty())
            avatarPool.add("user_image/default.png"); // fallback
    }

    @Override
    public void run(String... args) {

        if (userRepo.count() > 0) {
            System.out.println("DummySeeder ➜ data sudah ada, skip.");
            return;
        }

        System.out.println("DummySeeder ➜ seeding data… (avatar pool " + avatarPool.size() + " file)");
        String hash = enc.encode("password123");

        /* ---------- Mahasiswa ---------- */
        List<String[]> mahasiswaData = List.of(
                new String[] { "103012300231", "AHMAD REFI WIDI KATIBIN" },
                new String[] { "103012300425", "AL MADINATUL MUNAWARA" },
                new String[] { "103012300405", "ALI MOHAMMAD ARSYAD" },
                new String[] { "103012300420", "ANANDA MARCHEL HEATDA PUTRA" },
                new String[] { "103012300025", "APRILIANZA MUHAMMAD YUSUP" },
                new String[] { "103012300337", "ARFIAN GHIFARI MAHYA" },
                new String[] { "103012300055", "ARKANZI SUHARJANTO" },
                new String[] { "103012300260", "ARSYAD FILLAH" },
                new String[] { "103012300216", "ARSYAD MAULANA AHSAN" },
                new String[] { "103012300132", "ATHILA RAMDANI SAPUTRA" },
                new String[] { "103012300043", "AULIYA MARIFAH" },
                new String[] { "103012330197", "BILL STEPHEN" },
                new String[] { "103012300070", "BINTANG SAPUTRA" },
                new String[] { "103012300180", "DANDY FADILLA" },
                new String[] { "103012300522", "DAVINO PUTRA ARRAYA" },
                new String[] { "103012300391", "DZAKY ALFARIS" },
                new String[] { "103012330117", "EZRA MANGASI ANDIKA SIBUEA" },
                new String[] { "103012300303", "FADHILLA M WIRYASUKMA" },
                new String[] { "103012300149", "FARRAZ RAYA ABDILLAH" },
                new String[] { "103012300083", "FATHAN ARYA MAULANA" },
                new String[] { "103012300452", "GAVIN BENJIRO RAMADHAN" },
                new String[] { "103012300357", "JEANY FERLIZA NAYLA" },
                new String[] { "103012330009", "M RIFQI DZAKY AZHAD" },
                new String[] { "103012300165", "MUHAMMAD ARYA DWI KESUMA" },
                new String[] { "103012300436", "MUHAMMAD FAZLUR RAHMAN" },
                new String[] { "103012300288", "MUHAMMAD IHSAN NAUFAL" },
                new String[] { "103012300039", "MUHAMMAD IRGIANSYAH" },
                new String[] { "1301223461", "MUHAMMAD IRHAM ZIDNY" },
                new String[] { "103012300469", "MUHAMMAD NAYUBI ADIVA RAMADHAN" },
                new String[] { "103012300318", "MUHAMMAD RAFIF TAUFIQ RAMADHAN" },
                new String[] { "103012330531", "NABILA PUTRI AULIA" },
                new String[] { "103012300316", "NABILA PUTRI AZHARI" },
                new String[] { "103012300105", "NASYWA KAMILA" },
                new String[] { "103012300273", "NEVAN NABIIL FIRMANSYAH HAR" },
                new String[] { "103012330353", "RAFIQ LABIB" },
                new String[] { "103012300247", "SAMUEL YOHANES MICHAEL KAUNANG" },
                new String[] { "103012330375", "SENA RIZKY PRATAMA" },
                new String[] { "1301204393", "SYAHDAN RIDHO ALWALIDI" },
                new String[] { "1301223027", "TUBAGUS MAULANA ASMA'ULFAUZI RAFI'UDDIEN" },
                new String[] { "103012300100", "VEIRON VAYA YARIEF" },
                new String[] { "103012300203", "VELISIA NIHAN RAHMAWATI" },
                new String[] { "103012300150", "YOLANDA RAHMA CHRYSTI" });

        mahasiswaData.forEach(arr -> {
            Mahasiswa m = new Mahasiswa(arr[0], "Informatika");
            m.setName(arr[1]);
            m.setEmail(buildEmail(arr[1]));
            m.setPassword(hash);
            m.setRole("MAHASISWA");
            m.setFotoPath(randomAvatar());
            mhsRepo.save(m);
        });

        /* ---------- Staff Bardaks ---------- */
        List<String> staffNames = List.of(
                "Azhar alauddin S.T",
                "Rangga Pratama S.T",
                "Gifan Tattt S.Komedi",
                "Dandy Saputra S.Ekspektasi",
                "Maharditya Nugraha S.Sn",
                "Aziz Ramadhan S.Agak",
                "Rifky Putra S.Tidur",
                "Adit Panjaitan S.Kelam",
                "Bakti Pertiwi S.H",
                "Suci Lestari S.Psi",
                "Romero Ferdinand S.Kenang",
                "Ipam Saputra S.Kata",
                "Vanisa Pricillia S.Desain",
                "Aliya Zahra S.Gizi",
                "Azka Rahadian S.Kom");
        List<String> divisi = List.of(
                "Ketua", "Wakil", "Sekretaris", "Bendahara",
                "Humas", "Publikasi", "Kreatif", "Acara",
                "Sponsorship", "Logistik");

        staffNames.forEach(n -> {
            Staff s = new Staff(divisi.get(rand.nextInt(divisi.size())));
            s.setName(n);
            s.setEmail(buildEmail(n));
            s.setPassword(hash);
            s.setRole("STAFF");
            s.setFotoPath(randomAvatar());
            staffRepo.save(s);
        });

        System.out.println("DummySeeder ➜ selesai. total user: " + userRepo.count());
    }

    /* ---------- util ---------- */

    private String buildEmail(String nama) {
        String slug = Normalizer.normalize(nama, Normalizer.Form.NFD)
                .replaceAll("[^\\p{Alnum} ]", "")
                .trim()
                .replaceAll("\\s+", ".")
                .toLowerCase(Locale.ROOT);
        return slug + "@telyu.ac.id";
    }

    private String randomAvatar() {
        return avatarPool.get(rand.nextInt(avatarPool.size()));
    }
}
