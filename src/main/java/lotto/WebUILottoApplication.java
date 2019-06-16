package lotto;

import lotto.domain.LottoTickets;
import lotto.domain.Money;
import lotto.view.WebInputParser;
import lotto.view.WebOutputView;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebUILottoApplication {
    static Money money;
    static int numberOfManualLotto;
    static LottoTickets lottoTickets;

    public static void main(String[] args) {
        staticFiles.location("/templates");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        post("/number", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                money = WebInputParser.getMoney(req.queryParams("money"));
                model.put("money", money);
            } catch (Exception e) {
                model.put("error", e.getMessage());
                return render(model, "error.html");
            }

            return render(model, "number.html");
        });

        post("/manual", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                numberOfManualLotto = WebInputParser.getNumberOfManualLotto(money, req.queryParams("number"));
                if (numberOfManualLotto == 0) {
                    lottoTickets = WebInputParser.getLottoTickets(money, 0, null);
                    model.put("lottoTickets", WebOutputView.printLottoTicketsAsBall(lottoTickets));

                    return render(model, "winning.html");
                }

                model.put("number", numberOfManualLotto);
            } catch (Exception e) {
                model.put("error", e.getMessage());
                return render(model, "error.html");
            }

            return render(model, "manual.html");
        });

        post("/winning", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                lottoTickets = WebInputParser.getLottoTickets(money, numberOfManualLotto, req.queryParams("manual"));
                model.put("lottoTickets", WebOutputView.printLottoTicketsAsBall(lottoTickets));
            } catch (Exception e) {
                model.put("error", e.getMessage());
                return render(model, "error.html");
            }

            return render(model, "winning.html");
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
