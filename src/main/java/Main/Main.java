package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;
import java.util.Collections;


import org.apache.commons.cli.*;


class Main {

    public static void main(String[] args) {
        Options options = new Options();
        Option helpOpt = Option.builder("h")
                .longOpt("help")
                .desc("Usage Help")
                .build();
        options.addOption(helpOpt);
        Option name = Option.builder("n")
                .longOpt("name")
                .desc("Choosing files")
                .valueSeparator(' ')
                .hasArgs()
                .numberOfArgs(Option.UNLIMITED_VALUES)
                .build();
        options.addOption(name);
        Option short_stat = Option.builder("s")
                .longOpt("short")
                .desc("Show's short statistics")
                .build();
        options.addOption(short_stat);
        Option add_in_file = Option.builder("a")
                .longOpt("add")
                .desc("Add data in file")
                .build();
        options.addOption(add_in_file);
        Option path_to = Option.builder("o")
                .longOpt("out")
                .desc("Path to file")
                .hasArgs()
                .build();
        options.addOption(path_to);
        Option prefix_out = Option.builder("p")
                .longOpt("prefix")
                .desc("Add prefix to out")
                .hasArgs()
                .build();
        options.addOption(prefix_out);
        Option full_stat = Option.builder("f")
                .longOpt("full")
                .desc("Show full statistic ")
                .build();
        options.addOption(full_stat);
        BufferedReader reader;
        int int_count = 0, real_count = 0, string_count = 0;
        int int_sum = 0, int_min = 0, int_max = 0;
        double double_sum = 0, int_average = 0, double_average = 0, double_min = 0, double_max = 0;
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args, true);
            Vector<Integer> int_arr = new Vector();
            Vector<Double> double_arr = new Vector();
            Vector<String> string_arr = new Vector();

            try {
                String path = new File("").getAbsolutePath();
                String path_to_res = path;
                String prefix = "";
                if (cmd.hasOption(path_to)){
                    path_to_res = cmd.getOptionValue("o");
                    if (!Files.isDirectory(Path.of(path_to_res))) throw new IOException("Enter valid path");
                }
                if (cmd.hasOption(prefix_out)) {
                   prefix = cmd.getOptionValue("p") + "_";
                }

                if (!cmd.hasOption(add_in_file)) {
                    Files.deleteIfExists(Path.of(path_to_res + "\\" + prefix + "ints.txt"));
                    Files.deleteIfExists(Path.of(path_to_res + "\\" + prefix + "floats.txt"));
                    Files.deleteIfExists(Path.of(path_to_res + "\\" + prefix + "strings.txt"));
                }
                if (cmd.hasOption(name)) {



                    String[] FileNames;
                    FileNames = cmd.getOptionValues("n");
                    for (String fileName : FileNames) {
                        System.out.println(fileName);
                        reader = new BufferedReader(new FileReader(path + "\\" + fileName));
                        String line = reader.readLine();
                        while (line != null) {
                            try {
                                int num = 0;

                                FileWriter writer_int;
                                num = Integer.parseInt(line);

                                if (num != 0) {
                                    writer_int = new FileWriter(path_to_res + "\\" + prefix + "ints.txt", true);
                                    writer_int.write(num + "\n");
                                    writer_int.close();
                                    int_count++;
                                    int_arr.add(num);
                                }
                            } catch (Exception real) {
                                try {
                                    FileWriter writer_real;
                                    double real_num = 0;
                                    real_num = Double.parseDouble(line);
                                    writer_real = new FileWriter(path_to_res + "\\" + prefix + "floats.txt", true);
                                    writer_real.write(real_num + "\n");
                                    writer_real.close();
                                    real_count++;
                                    double_arr.add(real_num);
                                } catch (Exception str) {
                                    try {
                                        FileWriter writer_str;
                                        writer_str = new FileWriter(path_to_res + "\\" + prefix + "strings.txt", true);
                                        writer_str.write(line + "\n");
                                        writer_str.close();
                                        string_count++;
                                        string_arr.add(line);
                                    }
                                    finally {

                                    }
                                }
                            }
                            line = reader.readLine();
                        }
                        reader.close();
                    }
                }
                else if (!cmd.hasOption(helpOpt)){
                    throw new IOException("You need to choose files");
                }
                else {
                    System.out.println("-n or --name " + name.getDescription() +
                            ", sample: -n in1.txt in2.txt " + "\n" +
                            "-s or --short_stats " + short_stat.getDescription() + "\n" +
                            "-f or -full " + full_stat.getDescription() + "\n" +
                            "-a or --add " + add_in_file.getDescription() + "\n" +
                            "-o or --out " + path_to.getDescription() + ", sample: -o C:\\sample_dir" + "\n" +
                            "-p or --prefix " + prefix_out.getDescription() + ", sample: -p result");
                }

            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (cmd.hasOption(short_stat)) {
                System.out.println("Short statistic: \n" );
                System.out.println("Number of integer = " + int_count + "\n");
                System.out.println("Number of real = " + real_count + "\n");
                System.out.println("Number of strings = " + string_count + "\n");
            }

            if (cmd.hasOption(full_stat)) {
                int_min = Collections.min(int_arr);
                int_max = Collections.max(int_arr);
                double_min = Collections.min(double_arr);
                double_max = Collections.max(double_arr);
                for (int i = 1; i < int_arr.size(); i++) {
                    int_sum += int_arr.get(i);


                }
                for (int i = 1; i < double_arr.size(); i++) {
                    double_sum += double_arr.get(i);

                }
                int_average = (double) int_sum / int_arr.size();
                double_average = double_sum / double_arr.size();
                int str_min = Collections.min(string_arr).length();
                int str_max = Collections.max(string_arr).length();
                System.out.println("This is a full statistic: " + "\n");
                System.out.println("For integers: ");
                System.out.println("Total values " + int_count + ", minimum value is " + int_min + ", maximum value is "
                        + int_max + ", sum of all values is " + int_sum + ", average value is " + int_average + "\n");
                System.out.println("For floats: ");
                System.out.println("Total values " + real_count + ", minimum value is " + double_min + ", maximum value is "
                        + double_max + ", sum of all values is " + double_sum + ", average value is " + double_average + "\n");
                System.out.println("For strings: ");
                System.out.println("Total strings " + string_count + ", shortest string is "  + str_min + " characters length" +
                        ", longest string is "  + str_max + " characters length" );




            }

        } catch (MissingArgumentException e) {
            System.out.println("You need to enter names of files");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array out of bounds");
        }

    }

}