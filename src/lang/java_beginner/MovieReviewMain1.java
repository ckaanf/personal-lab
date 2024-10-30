package lang.java_beginner;

public class MovieReviewMain1 {
	public static void main(String[] args) {
		MovieReview[] movieReviews = new MovieReview[2];

		MovieReview inception = new MovieReview();
		inception.title = "인셉션";
		inception.review = "인생은 무한 루프";
		movieReviews[0] = inception;

		MovieReview aboutTime = new MovieReview();
		aboutTime.title = "어바웃 타임";
		aboutTime.review = "인생 시간 영화";
		movieReviews[1] = aboutTime;

		for (MovieReview movieReview : movieReviews) {
			System.out.println("영화 제목 :" + movieReview.title + "영화 리뷰: " + movieReview.review);
		}
		
	}
}
