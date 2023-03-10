package assignments.assignment1;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotaGenerator {
    private static final Scanner input = new Scanner(System.in);
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Method main, program utama kalian berjalan disini.
     */
    public static void main(String[] args) {
        int select = 4;

        while (select != 0) {
            printMenu();
            System.out.print("Pilihan : ");

            try {
                select = Integer.parseInt(input.next());
            } catch (Exception e) {
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
                continue;
            }


            switch (select){
                case 1 -> {
                    String nama, noHP = "";

                    System.out.println("Masukkan nama Anda:");
                    nama = input.next();

                    while (true) {
                        System.out.println("Masukkan nomor handphone Anda:");
                        noHP = input.next();
                        try {
                            Long.parseLong(noHP);
                        } catch (Exception e) {
                            System.out.println("Nomor hp tidak berupa angka");
                            continue;
                        }
                        break;
                    }

                    System.out.printf("ID Anda : %s\n", generateId(nama, noHP));
                }
                case 2 -> {
                    String nama, noHP, tanggal, paket;
                    int berat;

                    System.out.println("Masukkan nama Anda:");
                    nama = input.next();
                    input.next();


                    while (true) {
                        System.out.println("Masukkan nomor handphone Anda:");
                        noHP = input.next();
                        try {
                            Long.parseLong(noHP);
                        } catch (Exception e) {
                            System.out.println("Nomor hp tidak berupa angka");
                            continue;
                        }
                        break;
                    }

                    while (true) {
                        System.out.println("Masukkan tanggal terima:");
                        tanggal = input.next();
                        try {
                            LocalDate.parse(tanggal, format);
                        } catch (Exception e) {
                            System.out.println("Tanggal harus dalam bentuk dd/mm/yyyy.");
                            continue;
                        }
                        break;
                    }

                    while (true) {
                        boolean check = false;
                        System.out.println("Masukkan paket laundry:");
                        paket = input.next().toLowerCase();
                        switch (paket) {
                            case "express", "fast", "reguler"-> check = true;
                            case "?" -> showPaket();
                            default -> {
                                System.out.println("""
                                    Paket hemat tidak diketahui
                                    [ketik ? untuk mencari tahu jenis paket]""");
                            }
                        }
                        if (check) break;
                    }

                    while (true) {
                        System.out.println("Masukkan berat cucian Anda [Kg]:");
                        String strBerat = input.next();
                        try {
                            berat = Integer.parseInt(strBerat);
                        } catch (Exception e) {
                            System.out.println("Harap masukkan berat cucian Anda dalam bentuk bilangan positif.");
                            continue;
                        }
                        break;
                    }

                    System.out.println("Nota Laundry");
                    System.out.println(generateNota(generateId(nama, noHP), paket, berat, tanggal));

                }
                case 0 -> {}
                default -> System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }

        System.out.println("Terima kasih telah menggunakan NotaGenerator!");
        input.close();
    }

    /**
     * Method untuk menampilkan menu di NotaGenerator.
     */
    private static void printMenu() {
        System.out.println("Selamat datang di NotaGenerator!");
        System.out.println("==============Menu==============");
        System.out.println("[1] Generate ID");
        System.out.println("[2] Generate Nota");
        System.out.println("[0] Exit");
    }

    /**
     * Method untuk menampilkan paket.
     */
    private static void showPaket() {
        System.out.println("+-------------Paket-------------+");
        System.out.println("| Express | 1 Hari | 12000 / Kg |");
        System.out.println("| Fast    | 2 Hari | 10000 / Kg |");
        System.out.println("| Reguler | 3 Hari |  7000 / Kg |");
        System.out.println("+-------------------------------+");
    }

    /**
     * Method untuk membuat ID dari nama dan nomor handphone.
     * Parameter dan return type dari method ini tidak boleh diganti agar tidak mengganggu testing
     *
     * @return String ID anggota dengan format [NAMADEPAN]-[nomorHP]-[2digitChecksum]
     */
    public static String generateId(String nama, String nomorHP){
        nama = nama.split(" ")[0].toUpperCase();

        int total = 0;
        String out = nama + '-' + nomorHP;
        for (int i = 0; i < out.length(); i++){
            if (Character.isLetter(out.charAt(i))) {
                total += out.charAt(i) & 31;
            }
            else if (Character.isDigit(out.charAt(i))) {
                total += out.charAt(i) & 15;
            }
            else total += 7;

            total %= 100;
        }
        String totStr = Integer.toString(total);
        if (totStr.length() == 1) totStr = "0" + totStr;

        out += "-" + totStr;
        return out;
    }

    /**
     *
     * Method untuk membuat Nota.
     * Parameter dan return type dari method ini tidak boleh diganti agar tidak mengganggu testing.
     *
     * @return string nota dengan format di bawah:
     *         <p>ID    : [id]
     *         <p>Paket : [paket]
     *         <p>Harga :
     *         <p>[berat] kg x [hargaPaketPerKg] = [totalHarga]
     *         <p>Tanggal Terima  : [tanggalTerima]
     *         <p>Tanggal Selesai : [tanggalTerima + LamaHariPaket]
     */

    public static String generateNota(String id, String paket, int berat, String tanggalTerima){
        if (berat < 2) {
            System.out.println("Cucian kurang dari 2 kg, maka cucian akan dianggap sebagai 2 kg");
            berat = 2;
        }
        LocalDate startDate = LocalDate.parse(tanggalTerima, format);
        LocalDate endDate = startDate.plusDays(
                switch (paket) {
                    case "express" -> 1;
                    case "fast" -> 2;
                    case "reguler" -> 3;
                    default -> 0;
                }
        );

        int harga = switch (paket) {
            case "express" -> 12000;
            case "fast" -> 10000;
            case "reguler" -> 7000;
            default -> 0;
        };

        return String.format("""
            ID    : %s
            Paket : %s
            Harga : 
            %d kg x %d = %d
            Tanggal Terima  : %s
            Tanggal Selesai : %s""",
                id, paket, berat, harga, berat * harga,
                startDate.format(format), endDate.format(format));
    }
}
