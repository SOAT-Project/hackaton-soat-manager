package soat.project.hackaton_soat_manager.application.usecase;

public abstract class UnitUseCase<IN> {
    public abstract void execute(IN command);
}
