Описание программы:

Программа представляет из себя консольное приложение для пк с текстовым интерфейсом (запуск программы в Main.kt). 
Для хранения данных она использует MySQL базу данных на облаке.
Программа реализует основной требуемый функционал, команды в интерфейсе должны быть интуитивно понятны.
Однако я хочу выделить несколько нюансов работы программы.
У одного пользователя в один момент времени может быть только один заказ,
но он может его дополнять новыми блюдами, пока этот заказ в процессе готовки.
Для приготовления каждого блюда отводится свой поток.
Пример: Можно заказать несколько блюд за одного пользователя, переключиться на другого
и заказать ещё что-нибудь, тогда у нас получится ситуация, где 2 заказа выполняются параллельно (для каждого блюда свой поток),
но каждому пользователю соответствует только один заказ.
Для удобства в системе уже есть 2 пользователя и небольшой список блюд. Можете входить в систему с логином "a" и паролем "a"
в качестве гостя или с логином "b" и паролем "b" в качестве админа.

Паттерны проектирования:

Синглтон - использовал для SystemManager и UserInterface, потому что они могут быть только одни

Декоратор - в пакете extensions сделал удобные расширения, чтобы переводить число в булл и обратно

Фасад - это SystemManager, без него я бы не смог соединить все мои классы работы с данными
