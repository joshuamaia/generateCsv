package generateCSV;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class MainGenerateCsv {

	public record Usuario(Integer id, String nome, String email) implements CsvRow {
		@Override
		public String[] toCsvRow() {
			return new String[] { String.valueOf(id), nome, email };
		}
	}

	public record Categoria(Integer id, String nome) implements CsvRow {
		@Override
		public String[] toCsvRow() {
			return new String[] { String.valueOf(id), nome };
		}
	}

	public static void main(String[] args) {
		List<Usuario> usuarios = Arrays.asList(new Usuario(1, "Alice", "alice@example.com"),
				new Usuario(2, "Bob", "bob@example.com"), new Usuario(3, "Carol", "carol@example.com"));

		List<Categoria> categorias = Arrays.asList(new Categoria(1, "Eletrônicos"), new Categoria(2, "Roupas"),
				new Categoria(3, "Livros"));

		generateCsvFile("C:/joshua/usuarios.csv", usuarios, Usuario.class);
		generateCsvFile("C:/joshua/categorias.csv", categorias, Categoria.class);
	}

	public static void generateCsvFile(String filePath, List<? extends CsvRow> csvRows, Class<?> clazz) {
		String[] headers = extractHeaders(clazz);

		try (FileWriter writer = new FileWriter(filePath)) {
			writeLine(writer, headers);

			for (CsvRow csvRow : csvRows) {
				writeLine(writer, csvRow.toCsvRow());
			}

			System.out.println("CSV file was created successfully.");
		} catch (IOException e) {
			System.out.println("An error occurred while writing the CSV file.");
			e.printStackTrace();
		}
	}

	private static void writeLine(FileWriter writer, String[] fields) throws IOException {
		for (int i = 0; i < fields.length; i++) {
			writer.append(fields[i]);
			if (i < fields.length - 1) {
				writer.append(',');
			}
		}
		writer.append('\n');
	}

	private static String[] extractHeaders(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		String[] headers = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			headers[i] = capitalize(fields[i].getName());
		}
		return headers;
	}

	private static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

}
