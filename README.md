# Кеширование в памяти (LRU, LFU)

Тестовое задание для Java-разработчика

---

## LRU Cache

Данная реализация использует `LinkedHashMap<KEY, VALUE>` для хранения информации о ключах и соответствующих им значениях в порядке добавления.
Это предоставляет информацию о самом давно используемом ключе, находящемся в начале списка карты, и возможность его удаления в случае переполнения кеша.
Перемещение ключа в конец списка карты происходит путём его удаления и последующего добавления.
Ключ перемещаются в конец списка карты в случае его добавления или обращения к нему.

Всё это позволяет производить операции по добавлению / чтению / удалению элементов за константное O(1) время.

## LFU Cache

Данная реализация использует следующие структуры данных:
 - `HashMap<KEY, VALUE>` - для хранения информации о ключах и соответствующих им значениях.
 - `HashMap<FREQUENCY, LinkedHashSet<KEY>>` - для хранения информации о ключах в порядке их добавления и соответствующих им частотах использования.

Постоянное отслеживание минимальной частоты предоставляет возможность удаления соответствующего ключа в случае переполнения кеша.
Если таковых ключей несколько, то будет удален самый давно используемый ключ по аналогии со стратегией LRU.
Частота соответствующего ключа инкрементируются при его чтении или изменении связанного с ним значения.
Ранее не существующий ключ при добавлении примет нулевую частоту.
При инкрементировании частоты ключ удаляется из старого списка карты и добавляется к конец нового списка карты.

Всё это позволяет производить операции по добавлению / чтению элементов за константное O(1) время
и удалению элементов за O(n) время в худшем случае (при поиске новой минимальной частоты).