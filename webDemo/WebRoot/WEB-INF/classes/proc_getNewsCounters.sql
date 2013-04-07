-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE getNewsCounters(
	-- Add the parameters for the stored procedure here
	@fromDate DATETIME,
	@toDate DATETIME,
	@fromStrings VARCHAR(200))
AS
DECLARE @P INT
DECLARE @C INT
DECLARE @From VARCHAR(32)
DECLARE @i INT
DECLARE @count INT
SET @P=1 
SET @C=1
SET @i=0
BEGIN TRANSACTION
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	
	CREATE TABLE #tmpTable(tid int IDENTITY(1,1) primary key,tFroms varchar(255))

	SET @C=CHARINDEX(',',@fromStrings,@P+1)
	
	IF(@C=0)
		BEGIN
			INSERT INTO #tmpTable(tFroms)values(@fromStrings)
		END
	ELSE
		BEGIN
			SET @From=SUBSTRING(@fromStrings,@P,@C-@P)
		
			INSERT INTO #tmpTable(tFroms)values(@From)
			SET @P=@C
			WHILE (@P+1< LEN(@fromStrings))
				BEGIN
					SET @C=CHARINDEX(',',@fromStrings,@P+1)
					IF(@C> 0)
						BEGIN
							SET @From=SUBSTRING(@fromStrings,@P+1,@C-@P-1)
							INSERT INTO #tmpTable(tFroms)values(@From)
							SET @P=@C
						END
					ELSE
						BREAK  
				END
			SET @From=SUBSTRING(@fromStrings,@P+1,LEN(@fromStrings)-@P)
			INSERT INTO #tmpTable(tFroms)values(@From)
		END
	
	DECLARE db_cursor cursor for 
		SELECT #tmpTable.tFroms From #tmpTable
	OPEN db_cursor
	FETCH NEXT FROM db_cursor into @From
	
	while @@FETCH_STATUS = 0
		BEGIN
			SET @i=0
			while DATEADD(day,@i,@fromDate)<=@toDate
				BEGIN
					SELECT @count=COUNT(*) FROM news_count_table
					WHERE news_count_table.news_pubdate=DATEADD(day,@i,@fromDate)
					AND news_count_table.news_from=@From
					IF @count>0
						BEGIN
							UPDATE news_table SET news_count_id=(
								SELECT news_count_table.news_count_id
								FROM news_count_table
								WHERE news_count_table.news_from=@From AND news_count_table.news_pubdate=DATEADD(day,@i,@fromDate))
							WHERE news_table.news_from=@From AND news_table.news_pubdate=DATEADD(day,@i,@fromDate)
							
							UPDATE news_count_table SET news_count=(
								SELECT COUNT(*) FROM news_table nt 
								WHERE nt.news_from=@From AND nt.news_pubdate=DATEADD(day,@i,@fromDate))
							WHERE news_count_table.news_from=@From AND news_count_table.news_pubdate=DATEADD(day,@i,@fromDate)
						END
					ELSE IF @count=0
						BEGIN
							INSERT INTO news_count_table(news_from,news_pubdate,news_count)
							VALUES(@From,DATEADD(day,@i,@fromDate),(
								SELECT COUNT(*) FROM news_table nt 
								WHERE nt.news_from=@From AND nt.news_pubdate=DATEADD(day,@i,@fromDate))
							)
							
							UPDATE news_table SET news_count_id=(
								SELECT news_count_table.news_count_id
								FROM news_count_table
								WHERE news_count_table.news_from=@From AND news_count_table.news_pubdate=DATEADD(day,@i,@fromDate))
							WHERE news_table.news_from=@From AND news_table.news_pubdate=DATEADD(day,@i,@fromDate)
						END
					SET @i=@i+1
				END
			FETCH NEXT FROM db_cursor into @From
		END
	CLOSE db_cursor 
	drop table #tmpTable
	
	-- last results
	SELECT * FROM news_count_table 
	WHERE news_count_table.news_pubdate>=@fromDate 
	AND news_count_table.news_pubdate<=@toDate
	
	SET NOCOUNT OFF
	IF @@ERROR=0
		BEGIN
			COMMIT TRANSACTION
		END
	ELSE
		BEGIN
			ROLLBACK TRANSACTION
		END
GO
