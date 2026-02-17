package soat.project.hackaton_soat_manager.application.usecase;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN command);
}
