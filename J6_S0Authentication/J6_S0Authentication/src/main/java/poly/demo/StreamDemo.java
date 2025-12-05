package poly.demo;

import java.util.List;

public class StreamDemo {
	public static void main(String[] args) {
		List<Student> list = List.of(
				Student.builder().name("SV 01").gender(false).mark(6).build(),
				Student.builder().name("SV 02").gender(true).mark(8).build(),
				Student.builder().name("SV 03").gender(true).mark(9).build(),
				Student.builder().name("SV 04").gender(false).mark(5).build()
		);
		
		List<String> names = list.stream().filter(s -> s.getMark() > 7).map(s->s.getName()).toList();
		names.forEach(n -> System.out.println(n));
	}
}
